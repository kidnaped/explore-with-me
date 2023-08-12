package ru.practicum.event.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.comments.model.Comment;
import ru.practicum.event.comments.repository.CommentRepository;
import ru.practicum.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CommentServiceUtils {
    private final CommentRepository repository;

    protected Comment findById(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment " + commentId + " not found."));
    }
}
