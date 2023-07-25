package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategory(Category category);
}
