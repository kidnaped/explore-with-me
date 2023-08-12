package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestPublic;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {
    private final EventServiceUtils utils;
    private final EventRepository repository;

    @Override
    public List<EventFullDto> findEvents(EventSearchRequestPublic searchRequest) {
        log.info("Received PUBLIC_SEARCH_REQUEST {}",
                searchRequest.getText());

        LocalDateTime start = searchRequest.getRangeStart();
        LocalDateTime end = searchRequest.getRangeEnd();
        if (start != null && end != null) {
            Utils.startTimeValidation(start, end);
        }

        List<Event> events = repository.findByParameters(
                searchRequest.getText(),
                searchRequest.getCategories(),
                searchRequest.getPaid(),
                start,
                end,
                searchRequest.getOnlyAvailable(),
                searchRequest.getSort(),
                Utils.getPage(searchRequest.getFrom(), searchRequest.getSize()));
        List<EventFullDto> dtos = utils.makeFullDtosWithViews(events);
        log.info("Found {} events.", events.size());

        return dtos;
    }

    @Transactional
    @Override
    public EventFullDto getById(Long eventId) {
        log.info("Received EVENT_ID {}", eventId);

        Event event = utils.findById(eventId);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event " + eventId + " not found.");
        }
        EventFullDto dto = utils.makeFullDtosWithViews(List.of(event)).get(0);
        log.info("Event {}, {} is found.", event.getId(), event.getTitle());

        return dto;
    }
}
