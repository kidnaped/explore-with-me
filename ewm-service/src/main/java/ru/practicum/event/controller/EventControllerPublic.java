package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Utils;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestPublic;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.service.EventServicePublic;
import ru.practicum.event.statistics.StatService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    private final EventServicePublic service;
    private final StatService statSender;

    @GetMapping
    public List<EventFullDto> findEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) Set<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = Utils.FORMAT)
                                             LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = Utils.FORMAT)
                                             LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        statSender.send(servletRequest);

        EventSearchRequestPublic searchRequest = new EventSearchRequestPublic(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
        return service.findEvents(searchRequest);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable Long id,
                                HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        statSender.send(servletRequest);
        return service.getById(id);
    }
}
