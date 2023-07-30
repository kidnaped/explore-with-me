package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePrivate {
    List<EventFullDto> findEvents(Long userId, Integer from, Integer size, HttpServletRequest servletRequest);

    EventFullDto registerEvent(Long userId, NewEventDto dto);

    EventFullDto findByUsersEventById(Long userId, Long eventId, HttpServletRequest servletRequest);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> findRequestsForUsersEvent(Long userId,
                                                            Long eventId,
                                                            HttpServletRequest servletRequest);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest request);
}
