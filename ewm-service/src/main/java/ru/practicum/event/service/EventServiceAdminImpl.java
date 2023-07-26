package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestAdmin;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class EventServiceAdminImpl implements EventServiceAdmin {
    @Override
    public List<EventFullDto> findEvents(EventSearchRequestAdmin searchRequest, HttpServletRequest servletRequest) {
        return null;
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateRequest) {
        return null;
    }
}
