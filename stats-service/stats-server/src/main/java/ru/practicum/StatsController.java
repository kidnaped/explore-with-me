package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.Utils;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsRequest;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    public EndpointHitDto hit(@RequestBody EndpointHitDto dto) {
        log.info("Received hit request for dto {}", dto);
        return service.hit(dto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Received getStats request with parameters: START {}, END {}, URIS {}, UNIQUE {}",
                start, end, uris, unique);

        ViewStatsRequest request = ViewStatsRequest.builder()
                .start(LocalDateTime.parse(start, Utils.DTF))
                .end(LocalDateTime.parse(end, Utils.DTF))
                .uris(uris)
                .unique(unique)
                .build();
        return service.get(request);
    }
}
