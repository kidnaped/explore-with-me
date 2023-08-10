package ru.practicum.event.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsRequest;
import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatService {
    private final StatsClient client;
    @Value("${ewm.service.name}")
    private String serviceName;

    public void send(HttpServletRequest request) {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app(serviceName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        client.hit(dto);
    }

    public List<ViewStatsDto> retrieve(List<Event> events) {
        List<String> uris = events.stream()
                .map(Event::getId)
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());

        ViewStatsRequest request = ViewStatsRequest.builder()
                .start(Utils.START_TIME)
                .end(Utils.END_TIME)
                .uris(uris)
                .unique(true)
                .build();

        return client.getStats(request);
    }
}
