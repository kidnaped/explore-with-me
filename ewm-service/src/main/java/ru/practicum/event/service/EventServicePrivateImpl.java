package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryServiceUtils;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.model.StateActionUser;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.Status;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServicePrivateImpl implements EventServicePrivate {
    private final UserService userService;
    private final EventRepository repository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EventMapper mapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;
    private final EventServiceUtils utils;
    private final CategoryServiceUtils categoryUtils;

    @Transactional
    @Override
    public EventFullDto registerEvent(Long userId, NewEventDto dto) {
        log.info("Received USER_ID {}, NEW_EVENT_DTO {}", userId, dto.getTitle());

        utils.beforeEventTimeValidation(dto.getEventDate(), 2);

        User user = userService.findById(userId);
        Event event = makeFullEvent(user, dto);
        event = repository.save(event);
        log.info("Event registered: {}, {}.", event.getId(), event.getTitle());

        return mapper.toDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId,
                                    Long eventId,
                                    UpdateEventUserRequest request) {
        User user = userService.findById(userId);
        Event event = utils.findById(eventId);
        event = mapper.fromDto(event, request);
        StateActionUser stateAction = request.getStateAction();

        utils.beforeEventTimeValidation(event.getEventDate(), 2);

       validateHostOfEvent(event, user);

        if (event.getState().equals(State.PUBLISHED)) {
            throw new IllegalStateException("PUBLISHED events can't be changed.");
        }

        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }

        event = repository.save(utils.makeUpdatedEvent(event, request));
        log.info("Updated event: {}, {}", event.getId(), event.getTitle());

        return mapper.toDto(event);
    }

    @Override
    public List<EventFullDto> findEvents(Long userId,
                                         Integer from,
                                         Integer size,
                                         HttpServletRequest servletRequest) {
        log.info("Received USER_ID {}, FROM {}, SIZE {}, REQUEST {}",
                userId, from, size, servletRequest.getMethod());

        User user = userService.findById(userId);
        List<Event> events = repository.findAllByInitiatorId(user.getId(), Utils.getPage(from, size));
        List<EventFullDto> dtos = utils.toFullDtos(events);

        utils.addViews(dtos);
        log.info("Found {} events.", events.size());

        return dtos;
    }

    @Override
    public EventFullDto findUsersEventById(Long userId,
                                           Long eventId,
                                           HttpServletRequest servletRequest) {
        log.info("Received USER_ID {}, EVENT_ID {}, REQUEST {}",
                userId, eventId, servletRequest.getMethod());

        User user = userService.findById(userId);
        Event event = repository.findByIdAndInitiatorId(eventId, user.getId())
                .orElseThrow(() -> new NotFoundException("Event by user " + user.getId() + " not found."));
        EventFullDto dto = mapper.toDto(event);

        utils.addViews(List.of(dto));
        log.info("Event {}, {} found.", event.getId(), event.getTitle());

        return mapper.toDto(event);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsForUsersEvent(Long userId, Long eventId) {
        log.info("Received USER_ID {}, EVENT_ID {}", userId, eventId);

        User user = userService.findById(userId);
        Event event = utils.findById(eventId);
        if (!repository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException("Event " + event.getId() + " by user " + user.getId() + " not found.");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        log.info("Found {} participation requests.", requests.size());

        return requestMapper.toDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest requestDto) {
        log.info("Received USER_ID {}, EVENT_ID {}, REQUEST {}", userId, eventId, requestDto);

        User user = userService.findById(userId);
        Event event = utils.findById(eventId);

        validateHostOfEvent(event, user);

        if (event.getConfirmedRequests() + requestDto.getRequestIds().size() > event.getParticipantLimit()) {
            throw new IllegalStateException("Reached participation limit for this event.");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(requestDto.getRequestIds());
        requests.forEach(req -> {
            if (req.getStatus().equals(Status.CONFIRMED)) {
                throw new IllegalStateException("Participation request has been already approved.");
            }
        });
        requests.forEach(req -> req.setStatus(requestDto.getStatus()));

        event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());

        requestRepository.saveAll(requests);
        repository.save(event);

        List<ParticipationRequest> confirmedRequests = requestRepository
                .findAllByEventIdAndStatus(event.getId(), Status.CONFIRMED);
        List<ParticipationRequest> rejectedRequests = requestRepository
                .findAllByEventIdAndStatus(event.getId(), Status.REJECTED);
        log.info("{} participation requests updated.", requestDto.getRequestIds().size());

        return new EventRequestStatusUpdateResult(
                requestMapper.toDto(confirmedRequests),
                requestMapper.toDto(rejectedRequests)
        );
    }

    private Event makeFullEvent(User user, NewEventDto dto) {

        Event event = mapper.fromDto(dto);
        Category category = categoryUtils.findById(dto.getCategory());

        Location location = locationMapper.fromDto(dto.getLocation());
        Float lat = location.getLat();
        Float lon = location.getLon();
        location = locationRepository.existsByLatAndLon(lat, lon)
                ? locationRepository.findByLatAndLon(lat, lon)
                : locationRepository.save(location);

        event.setInitiator(user);
        event.setLocation(location);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0L);
        event.setState(State.PENDING);
        event.setPaid(dto.getPaid() != null ? dto.getPaid() : false);
        event.setParticipantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0);
        event.setRequestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true);

        return event;
    }

    private void validateHostOfEvent(Event event, User user) {
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new IllegalStateException("Can't update not hosted event.");
        }
    }
}
