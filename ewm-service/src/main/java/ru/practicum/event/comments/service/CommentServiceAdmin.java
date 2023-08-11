package ru.practicum.event.comments.service;

import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.CommentSearchRequest;

import java.util.List;

public interface CommentServiceAdmin {
    List<CommentDto> getAllByRequest(CommentSearchRequest searchRequest);

    void deleteComment(Long commentId);
}
