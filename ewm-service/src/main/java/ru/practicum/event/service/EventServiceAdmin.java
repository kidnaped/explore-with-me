package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchRequestAdmin;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServiceAdmin {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateRequest);

    List<EventFullDto> findEvents(EventSearchRequestAdmin searchRequest, HttpServletRequest servletRequest);
}
