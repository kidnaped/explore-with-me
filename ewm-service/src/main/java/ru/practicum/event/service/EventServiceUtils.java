package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryServiceUtils;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceUtils {
    private final EventRepository repository;
    private final CategoryServiceUtils categoryUtils;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    public Event findById(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event " + eventId + " not found."));
    }

    protected void addViews(List<Event> events) {
        events.forEach(event -> event.setViews(event.getViews() == null ? 1 : event.getViews() + 1));
        repository.saveAll(events);
    }

    protected void beforeEventTimeValidation(LocalDateTime eventTime, Integer hours) {
        if (eventTime != null && eventTime.isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new ValidationException("Event must be no less than " + hours + " hours before update.");
        }
    }

    protected void startTimeValidation(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.equals(end)) {
            throw new ValidationException("Start time must be not after or equals end time.");
        }
    }

    protected <T extends UpdateEventRequest> Event makeUpdatedEvent(Event event, T request) {
        Long categoryId = request.getCategory();
        LocationDto locationDto = request.getLocation();

        if (categoryId != null) {
            Category category = categoryUtils.findById(categoryId);
            event.setCategory(category);
        }

        if (locationDto != null) {
            Location location = locationMapper.fromDto(locationDto);
            Float lat = location.getLat();
            Float lon = location.getLon();

            if (locationRepository.existsByLatAndLon(lat, lon)) {
                location = locationRepository.findByLatAndLon(lat, lon);
            } else {
                location = locationRepository.save(location);
            }
            event.setLocation(location);
        }
        return event;
    }
}
