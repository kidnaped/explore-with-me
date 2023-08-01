package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventServicePrivate;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventServicePrivate service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto register(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto dto,
                                 HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.registerEvent(userId, dto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateRequest,
                               HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.updateEvent(userId, eventId, updateRequest);
    }

    @GetMapping
    public List<EventFullDto> findEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.findEvents(userId, from, size, servletRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findUsersEvent(@PathVariable Long userId,
                                       @PathVariable Long eventId,
                                       HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.findUsersEventById(userId, eventId, servletRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsForEvent(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.findRequestsForUsersEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @Valid @RequestBody
                                                                  EventRequestStatusUpdateRequest requestDto,
                                                              HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.updateRequestStatus(userId, eventId, requestDto);
    }
}
