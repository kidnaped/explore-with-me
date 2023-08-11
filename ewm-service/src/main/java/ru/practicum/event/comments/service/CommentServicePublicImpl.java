package ru.practicum.event.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.mapper.CommentMapper;
import ru.practicum.event.comments.model.Comment;
import ru.practicum.event.comments.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventServiceUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServicePublicImpl implements CommentServicePublic {
    private final EventServiceUtils eventServiceUtils;
    private final CommentMapper mapper;
    private final CommentRepository repository;

    @Override
    public List<CommentDto> getAllByEvent(Long eventId, Integer from, Integer size) {
        log.info("Received EVENT_ID {}, FROM {}, SIZE {}", eventId, from, size);

        Event event = eventServiceUtils.findById(eventId);
        List<Comment> comments = repository.findAllByEventId(event.getId(), Utils.getPage(from, size));
        log.info("Found {} comments.", comments.size());

        return mapper.toDto(comments);
    }
}
