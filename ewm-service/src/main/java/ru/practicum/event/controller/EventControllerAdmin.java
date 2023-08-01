package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Utils;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestAdmin;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventServiceAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventServiceAdmin service;

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest updateRequest,
                               HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.update(eventId, updateRequest);
    }

    @GetMapping
    public List<EventFullDto> findEvents(@RequestParam(required = false) Set<Long> users,
                                         @RequestParam(required = false) Set<State> states,
                                         @RequestParam(required = false) Set<Long> categories,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = Utils.FORMAT)
                                             LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = Utils.FORMAT)
                                             LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        EventSearchRequestAdmin searchRequest = EventSearchRequestAdmin.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        return service.findEvents(searchRequest, servletRequest);
    }
}
