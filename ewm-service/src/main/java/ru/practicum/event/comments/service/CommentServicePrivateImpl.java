package ru.practicum.event.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.NewCommentDto;
import ru.practicum.event.comments.mapper.CommentMapper;
import ru.practicum.event.comments.model.Comment;
import ru.practicum.event.comments.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventServiceUtils;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServicePrivateImpl implements CommentServicePrivate {
    private final CommentServiceUtils utils;
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final UserService userService;
    private final EventServiceUtils eventServiceUtils;

    @Transactional
    @Override
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto dto) {
        log.info("Received USER_ID {}, EVENT_ID {}, NEW_COMMENT_DTO {}", userId, eventId, dto);

        User user = userService.findById(userId);
        Event event = eventServiceUtils.findById(eventId);
        Comment comment = mapper.fromDto(dto);

        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());

        comment = repository.save(comment);
        log.info("Comment created: {}, {}", comment.getId(), comment.getText());

        return mapper.toDto(comment);
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto) {
        log.info("Received USER_ID {}, EVENT_ID {}, NEW_COMMENT_DTO {}", userId, commentId, dto);

        User user = userService.findById(userId);
        Comment comment = utils.findById(commentId);

        validateCommentOwner(user, comment);

        comment = mapper.fromDto(comment, dto);
        comment.setCreated(LocalDateTime.now());
        log.info("Comment {} updated.", comment.getId());

        return mapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getUsersComments(Long userId, Integer from, Integer size) {
        log.info("Received USER_ID {}, FROM {}, SIZE {}", userId, from, size);

        User user = userService.findById(userId);
        List<Comment> comments = repository.findAllByAuthorId(user.getId(), Utils.getPage(from, size));
        log.info("Found {} comments", comments.size());

        return mapper.toDto(comments);
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        log.info("Received USER_ID {}, COMMENTT_ID {}", userId, commentId);

        User user = userService.findById(userId);
        Comment comment = utils.findById(commentId);

        validateCommentOwner(user, comment);

        repository.deleteById(comment.getId());
        log.info("Comment {} deleted", comment.getId());
    }

    private void validateCommentOwner(User user, Comment comment) {
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new IllegalStateException("User has no comments with ID " + comment.getId());
        }
    }
}
