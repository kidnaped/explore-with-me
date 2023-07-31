package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestAdmin;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.model.StateActionAdmin;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.statistics.StatSenderService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository repository;
    private final EventServiceUtils utils;
    private final EventMapper mapper;
    private final StatSenderService statSender;

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateRequest) {
        log.info("Received EVENT ID {}, UPDATE_REQUEST {}", eventId, updateRequest);

        Integer hours = 1;
        StateActionAdmin stateAction = updateRequest.getStateAction();
        Event event = utils.findById(eventId);
        event = mapper.fromDto(event, updateRequest);

        utils.beforeEventTimeValidation(event.getEventDate(), hours);
        setEventStateByAdminRequest(updateRequest, stateAction, event);
        event = repository.save(utils.makeUpdatedEvent(event, updateRequest));
        log.info("Event {} updated by Admin.", event);

        return mapper.toDto(event);
    }

    @Override
    public List<EventFullDto> findEvents(EventSearchRequestAdmin searchRequest, HttpServletRequest servletRequest) {
        log.info("Received search request by Admin {} and HttpServletRequest {}",
                searchRequest, servletRequest.getMethod());

        LocalDateTime start = searchRequest.getRangeStart();
        LocalDateTime end = searchRequest.getRangeEnd();
        if (start != null && end != null) {
            utils.startTimeValidation(start, end);
        }

        List<Event> events = repository.findByParameters(
                searchRequest.getUsers(),
                searchRequest.getStates(),
                searchRequest.getCategories(),
                start,
                end,
                Utils.getPage(searchRequest.getFrom(), searchRequest.getSize()));

        utils.addViews(events);
        log.info("Found {} events.", events.size());

        statSender.send(servletRequest);
        return mapper.toDto(events);
    }

    private void setEventStateByAdminRequest(UpdateEventAdminRequest updateRequest, StateActionAdmin stateAction, Event event) {
        if (stateAction != null) {
            switch (updateRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    if (!event.getState().equals(State.PENDING)) {
                        throw new IllegalStateException("Event state must be PENDING to PUBLISH.");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    if (event.getState().equals(State.PUBLISHED)) {
                        throw new IllegalStateException("Event must NOT be PUBLISHED to REJECT.");
                    }
                    event.setState(State.CANCELED);
                    break;
                default:
                    throw new IllegalStateException("Requesting not supported action: " + stateAction);
            }
        }
    }
}
