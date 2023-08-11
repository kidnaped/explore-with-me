package ru.practicum.event.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.comments.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment as c " +
            "where (lower(c.text) like lower(concat('%', :text, '%')) or :text = null) " +
            "and (c.author.id in :users or :users = null) " +
            "and (c.event.id in :events or :events = null) " +
            "and ((:rangeStart != null and :rangeEnd != null " +
            "and c.created between :rangeStart and :rangeEnd) " +
            "or (:rangeStart = null and c.created < :rangeEnd) " +
            "or (:rangeEnd = null and c.created > :rangeStart) " +
            "or (:rangeStart = null and :rangeEnd = null))")
    List<Comment> findByRequest(@Param("text") String text,
                                @Param("users") Set<Long> users,
                                @Param("events") Set<Long> events,
                                @Param("rangeStart")LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable pageable);

    List<Comment> findAllByAuthorId(Long userId, Pageable pageable);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);
}
