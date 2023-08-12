package ru.practicum.event.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.NewCommentDto;
import ru.practicum.event.comments.service.CommentServicePrivate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentControllerPrivate {
    private final CommentServicePrivate service;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @Valid @RequestBody NewCommentDto dto,
                             HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.createComment(userId, eventId, dto);
    }

    @PatchMapping("{commentId}")
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable Long commentId,
                             @Valid @RequestBody NewCommentDto dto,
                             HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.updateComment(userId, commentId, dto);
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId,
                       HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        service.deleteComment(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getAllByUser(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.getUsersComments(userId, from, size);
    }
}
