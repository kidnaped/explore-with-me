package ru.practicum.event.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Utils;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.CommentSearchRequest;
import ru.practicum.event.comments.service.CommentServiceAdmin;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class CommentControllerAdmin {
    private final CommentServiceAdmin service;

    @GetMapping
    public List<CommentDto> getAllBySearchRequest(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) Set<Long> users,
                                                  @RequestParam(required = false) Set<Long> events,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = Utils.FORMAT)
                                                  LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = Utils.FORMAT)
                                                  LocalDateTime rangeEnd,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                                  HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        CommentSearchRequest searchRequest = new CommentSearchRequest(
                text,
                users,
                events,
                rangeStart,
                rangeEnd,
                from,
                size
        );
        return service.getAllByRequest(searchRequest);
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId,
                       HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        service.deleteComment(commentId);
    }
}
