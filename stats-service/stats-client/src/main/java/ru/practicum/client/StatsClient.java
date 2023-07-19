package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatsClient {
    private final String serverUrl;
    private final RestTemplate restTemplate;

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
        this.restTemplate = new RestTemplate();
    }

    public void hit(EndpointHitDto dto) {
        log.info("Received dto with parameters: APP {}, URI {}, IP {}, TIME {} ",
                dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String fullUrl = serverUrl + "/hit";
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(dto, headers);
        restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStats(ViewStatsRequest vsRequest) {
        log.info("Received request with parameters: START {}, END {}, URIS {}, UNIQUE {}",
                vsRequest.getStart(), vsRequest.getEnd(), vsRequest.getUris(), vsRequest.getUnique());

        ObjectMapper mapper = new ObjectMapper();
        String fullUri = serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        Map<String, Object> params = new HashMap<>();
        params.put("start", vsRequest.getStart());
        params.put("end", vsRequest.getEnd());
        params.put("uris", List.of(vsRequest.getUris()));
        params.put("unique", vsRequest.getUnique());

        ResponseEntity<String> response = restTemplate.getForEntity(fullUri, String.class, params);
        try {
            ViewStatsDto[] dtos = mapper.readValue(response.getBody(), ViewStatsDto[].class);
            log.info("Returning {} ViewStatsDtos", dtos.length);
            return Arrays.asList(dtos);
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException caught: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
