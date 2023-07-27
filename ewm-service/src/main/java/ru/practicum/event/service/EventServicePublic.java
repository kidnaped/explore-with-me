package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestPublic;
import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {
    List<EventFullDto> findEvents(EventSearchRequestPublic searchRequest, HttpServletRequest servletRequest);

    EventFullDto getById(Long eventId, HttpServletRequest servletRequest);

    Event findById(Long eventId);
}
