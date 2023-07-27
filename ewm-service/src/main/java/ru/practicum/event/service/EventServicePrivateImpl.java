package ru.practicum.event.service;

import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class EventServicePrivateImpl implements EventServicePrivate {
    @Override
    public List<EventFullDto> findEvents(Long userId,
                                         Integer from,
                                         Integer size,
                                         HttpServletRequest servletRequest) {
        return null;
    }

    @Override
    public EventFullDto registerEvent(Long userId, NewEventDto dto) {
        return null;
    }

    @Override
    public EventFullDto findByUsersEventById(Long userId,
                                             Long eventId,
                                             HttpServletRequest servletRequest) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long userId,
                                    Long eventId,
                                    UpdateEventUserRequest request) {
        return null;
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

    @Override
    public Event findById(Long eventId) {
        return null;
    }
}
