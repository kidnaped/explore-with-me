package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategory(Category category);

    boolean existsByIdAndInitiatorId(Long eventId, Long userId);

    @Query("select e from Event as e " +
            "where (e.initiator.id in :users OR :users = null) " +
            "and (e.state in :states OR :states = null) " +
            "and (e.category.id in :categories OR :categories = null) " +
            "and ((cast(:rangeStart as date) != null and cast(:rangeStart as date) != null " +
            "and e.eventDate between cast(:rangeStart as date) and cast(:rangeEnd as date)) " +
            "or (cast(:rangeStart as date) = null and e.eventDate < cast(:rangeEnd as date)) " +
            "or (cast(:rangeEnd as date) = null and e.eventDate > cast(:rangeStart as date)) " +
            "or (cast(:rangeStart as date) = null and cast(:rangeStart as date) = null)) ")
    List<Event> findByParameters(@Param("users") Set<Long> users,
                                 @Param("states") Set<State> states,
                                 @Param("categories") Set<Long> categories,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 Pageable pageable);

    @Query(" select e from Event as e " +
            "where (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')) " +
            "or lower(e.title) like lower(concat('%', :text, '%'))" +
            "or :text = null) " +
            "and (e.category.id in :categories or :categories = null) " +
            "and (e.paid = :paid or :paid = null) " +
            "and ((cast(:rangeStart as date) != null and cast(:rangeStart as date) != null " +
            "and e.eventDate between cast(:rangeStart as date) and cast(:rangeEnd as date) ) " +
            "or (cast(:rangeStart as date) = null and e.eventDate < cast(:rangeEnd as date) )" +
            "or (cast(:rangeEnd as date) = null and e.eventDate > cast(:rangeStart as date) )" +
            "or (cast(:rangeStart as date) = null and cast(:rangeStart as date) = null) " +
            "and (e.confirmedRequests < e.participantLimit or :onlyAvailable = null)) " +
            "and (e.state = 'PUBLISHED') " +
            "order by :sort")
    List<Event> findByParameters(@Param("text") String text,
                                 @Param("categories") Set<Long> categories,
                                 @Param("paid") Boolean paid,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 @Param("onlyAvailable") Boolean onlyAvailable,
                                 @Param("sort") EventSort sort,
                                 Pageable pageable);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findAllByIdIn(Set<Long> ids);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
}
