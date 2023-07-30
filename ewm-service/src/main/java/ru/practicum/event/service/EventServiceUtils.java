package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceUtils {
    public Event findById(Long eventId, EventRepository repository) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event " + eventId + " not found."));
    }

    public void addViews(List<Event> events, EventRepository repository) {
        events.forEach(event -> event.setViews(event.getViews() == null ? 1 : event.getViews() + 1));
        repository.saveAll(events);
    }

    public void beforeEventTimeValidation(LocalDateTime eventTime, Integer hours) {
        if (eventTime != null && eventTime.isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new ValidationException("Event must be no less than " + hours + " hours before update.");
        }
    }

    public void startTimeValidation(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.equals(end)) {
            throw new ValidationException("Start time must be not after or equals end time.");
        }
    }
}
