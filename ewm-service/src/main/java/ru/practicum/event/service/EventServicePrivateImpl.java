package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import ru.practicum.event.statistics.StatSenderService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
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
    private final StatSenderService statSender;
    private final EventRepository repository;
    private final LocationRepository locationRepository;
    private final EventMapper mapper;
    private final LocationMapper locationMapper;
    private final EventServiceUtils utils;
    private final CategoryServiceUtils categoryUtils;

    @Override
    public EventFullDto registerEvent(Long userId, NewEventDto dto) {
        log.info("Received USER_ID {}, NEW_EVENT_DTO {}", userId, dto);

        User user = userService.findById(userId);
        Event event = makeFullEvent(user, dto);
        event = repository.save(event);
        log.info("Event registered: {}.", event);

        return mapper.toDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId,
                                    Long eventId,
                                    UpdateEventUserRequest request) {
        User user = userService.findById(userId);
        Event event = utils.findById(eventId);
        event = mapper.fromDto(event, request);
        StateActionUser stateAction = request.getStateAction();

        utils.beforeEventTimeValidation(event.getEventDate(), 2);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new IllegalStateException("Can't update not hosted event.");
        }

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
                default:
                    throw new IllegalStateException("Requesting not supported action: " + stateAction);
            }
        }

        event = repository.save(utils.makeUpdatedEvent(event, request));
        log.info("Updated event: {}", event);

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

        utils.addViews(events);
        log.info("Found {} events.", events.size());

        statSender.send(servletRequest);
        return mapper.toDto(events);
    }

    @Override
    public EventFullDto findByUsersEventById(Long userId,
                                             Long eventId,
                                             HttpServletRequest servletRequest) {
        log.info("Received USER_ID {}, EVENT_ID {}, REQUEST {}",
                userId, eventId, servletRequest.getMethod());

        User user = userService.findById(userId);
        Event event = repository.findByIdAndInitiatorId(eventId, user.getId())
                .orElseThrow(() -> new NotFoundException("Event by user " + user.getId() + " not found."));

        utils.addViews(List.of(event));
        log.info("Event {} found.", event);

        statSender.send(servletRequest);
        return mapper.toDto(event);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsForUsersEvent(Long userId,
                                                                   Long eventId,
                                                                   HttpServletRequest servletRequest) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        return null;
    }

    private Event makeFullEvent(User user, NewEventDto dto) {
        utils.beforeEventTimeValidation(dto.getEventDate(), 2);
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
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);
        event.setViews(0);
        event.setPaid(dto.getPaid() != null ? dto.getPaid() : false);
        event.setParticipantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0);
        event.setRequestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true);

        return event;
    }
}
