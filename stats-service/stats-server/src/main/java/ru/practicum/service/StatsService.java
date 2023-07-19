package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsRequest;

import java.util.List;

public interface StatsService {
    EndpointHitDto hit(EndpointHitDto dto);

    List<ViewStatsDto> get(ViewStatsRequest request);
}
