package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestPublic;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.statistics.StatSenderService;
import ru.practicum.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {
    private final EventServiceUtils utils;
    private final EventRepository repository;
    private final EventMapper mapper;
    private final StatSenderService statSender;

    @Transactional
    @Override
    public List<EventFullDto> findEvents(EventSearchRequestPublic searchRequest, HttpServletRequest servletRequest) {
        log.info("Received public search request {} and HttpServletRequest {}",
                searchRequest, servletRequest.getMethod());

        LocalDateTime start = searchRequest.getRangeStart();
        LocalDateTime end = searchRequest.getRangeEnd();
        if (start != null && end != null) {
            utils.startTimeValidation(start, end);
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

        utils.addViews(events);
        log.info("Found {} events.", events.size());

        statSender.send(servletRequest);
        return mapper.toDto(events);
    }

    @Transactional
    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest servletRequest) {
        Event event = utils.findById(eventId);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event " + eventId + " not found.");
        }

        utils.addViews(List.of(event));
        log.info("Event {} is found.", event);

        statSender.send(servletRequest);
        return mapper.toDto(event);
    }
}
