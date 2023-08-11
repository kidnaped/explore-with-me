package ru.practicum.event.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.CommentSearchRequest;
import ru.practicum.event.comments.mapper.CommentMapper;
import ru.practicum.event.comments.model.Comment;
import ru.practicum.event.comments.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceAdminImpl implements CommentServiceAdmin {
    private final CommentServiceUtils utils;
    private final CommentRepository repository;
    private final CommentMapper mapper;

    @Override
    public List<CommentDto> getAllByRequest(CommentSearchRequest searchRequest) {
        log.info("Received SEARCH_REQUEST {}", searchRequest.getText());

        if (searchRequest.getRangeStart() != null && searchRequest.getRangeEnd() != null) {
            Utils.startTimeValidation(searchRequest.getRangeStart(), searchRequest.getRangeEnd());
        }

        List<Comment> comments = repository.findByRequest(
                searchRequest.getText(),
                searchRequest.getUsers(),
                searchRequest.getEvents(),
                searchRequest.getRangeStart(),
                searchRequest.getRangeEnd(),
                Utils.getPage(searchRequest.getFrom(), searchRequest.getSize())
        );
        log.info("Found {} comments", comments.size());

        return mapper.toDto(comments);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        log.info("Received COMMENT_ID {}", commentId);

        Comment comment = utils.findById(commentId);
        repository.deleteById(comment.getId());
        log.info("Comment {} deleted.", comment.getId());
    }
}
