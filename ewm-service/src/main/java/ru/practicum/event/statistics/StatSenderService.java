package ru.practicum.event.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatSenderService {
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
}
