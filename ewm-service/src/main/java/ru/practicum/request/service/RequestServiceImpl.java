package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServiceUtils;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.Status;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final RequestMapper mapper;
    private final UserService userService;
    private final EventServiceUtils eventUtils;

    @Override
    public ParticipationRequestDto register(Long userId, Long eventId) {
        log.info("Received USER ID {}, EVENT ID {}", userId, eventId);

        User user = userService.findById(userId);
        Event event = eventUtils.findById(eventId);

        if (userId.equals(event.getInitiator().getId())) {
            throw new IllegalStateException("User " + userId + " trying to request owned event.");
        }

        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("User " + userId + " already requested event.");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IllegalStateException("Event " + eventId + " is not published.");
        }

        if (!event.getParticipantLimit().equals(0)
                && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new IllegalStateException("Event " + event + " reached participant limit.");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .status(Status.PENDING)
                .created(LocalDateTime.now())
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        request = repository.save(request);
        log.info("Request {} registered.", request);

        return mapper.toDto(request);
    }

    @Override
    public ParticipationRequestDto updateRequest(Long userId, Long requestId) {
        log.info("Received USER ID {}, REQUEST ID {}", userId, requestId);

        ParticipationRequest request = repository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request " + requestId
                        + " by user " + userId
                        + " not found."));
        request.setStatus(Status.CANCELED);
        request = repository.save(request);
        log.info("Request {} canceled.", request);

        return mapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> findByUserId(Long userId) {
        log.info("Received USER ID {}", userId);

        User user = userService.findById(userId);
        List<ParticipationRequest> requests = repository.findAllByRequesterId(user.getId());
        log.info("Found {} events.", requests.size());

        return mapper.toDto(requests);
    }
}
