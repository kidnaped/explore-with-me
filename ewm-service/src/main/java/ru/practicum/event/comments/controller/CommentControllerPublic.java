package ru.practicum.event.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.service.CommentServicePublic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class CommentControllerPublic {
    private final CommentServicePublic service;

    @GetMapping
    public List<CommentDto> getAllByEvent(@PathVariable Long eventId,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                          HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.getAllByEvent(eventId, from, size);
    }
}
