package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryServiceUtils;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.statistics.StatService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceUtils {
    private final EventRepository repository;
    private final CategoryServiceUtils categoryUtils;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final StatService statService;
    private final EventMapper eventMapper;

    public Event findById(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event " + eventId + " not found."));
    }

    protected void addViews(List<EventFullDto> events) {
        List<ViewStatsDto> stats = statService.retrieve(events);
        Map<Long, Long> eventsViews = new HashMap<>();

        stats.forEach(stat -> {
            Long eventId = Long.parseLong(stat.getUri().split("/", 0)[2]);
            eventsViews.put(eventId, stat.getHits());
        });

        for (EventFullDto event : events) {
            event.setViews(eventsViews.getOrDefault(event.getId(), 0L));
        }
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

    protected List<EventFullDto> toFullDtos(List<Event> events) {
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }
}
