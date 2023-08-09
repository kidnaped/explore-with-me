package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestPublic;

import java.util.List;

public interface EventServicePublic {
    List<EventFullDto> findEvents(EventSearchRequestPublic searchRequest);

    EventFullDto getById(Long eventId);
}
