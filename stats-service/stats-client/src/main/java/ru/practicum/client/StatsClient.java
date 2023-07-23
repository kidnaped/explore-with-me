package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.Utils;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsRequest;

import java.util.List;

@Slf4j
@Service
public class StatsClient {
    private final WebClient client;
    private static final String HIT = "/hit";
    private static final String STATS = "/stats";

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public EndpointHitDto hit(EndpointHitDto dto) {
        log.info("Received dto with parameters: APP {}, URI {}, IP {}, TIME {} ",
                dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());

        return client.post()
                .uri(HIT)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStatsDto> getStats(ViewStatsRequest vsRequest) {
        log.info("Received request with parameters: START {}, END {}, URIS {}, UNIQUE {}",
                vsRequest.getStart(), vsRequest.getEnd(), vsRequest.getUris(), vsRequest.getUnique());

        return client.get()
                .uri(uriBuilder -> uriBuilder.path(STATS)
                        .queryParam("start", vsRequest.getStart().format(Utils.DTF))
                        .queryParam("end", vsRequest.getEnd().format(Utils.DTF))
                        .queryParam("uris", vsRequest.getUris())
                        .queryParam("unique", vsRequest.getUnique())
                        .build())
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collectList()
                .block();
    }
}
