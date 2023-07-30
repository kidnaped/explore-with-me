package ru.practicum.event.service;

import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestPublic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class EventServicePublicImpl implements EventServicePublic {
    @Override
    public List<EventFullDto> findEvents(EventSearchRequestPublic searchRequest, HttpServletRequest servletRequest) {
        return null;
    }

    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest servletRequest) {
        return null;
    }
}
