package ru.practicum.event.comments.service;

import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentServicePrivate {
    CommentDto createComment(Long userId, Long eventId, NewCommentDto dto);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto);

    List<CommentDto> getUsersComments(Long userId, Integer from, Integer size);

    void deleteComment(Long userId, Long commentId);
}
