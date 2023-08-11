package ru.practicum.event.comments.service;

import ru.practicum.event.comments.dto.CommentDto;

import java.util.List;

public interface CommentServicePublic {
    List<CommentDto> getAllByEvent(Long eventId, Integer from, Integer size);
}
