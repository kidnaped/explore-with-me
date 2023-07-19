package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsRepository;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsRequest;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final ViewStatsMapper vMapper;
    private final EndpointHitMapper ehMapper;
    private final StatsRepository repository;

    @Transactional
    @Override
    public EndpointHitDto hit(EndpointHitDto dto) {
        log.info("Received dto with parameters: APP {}, URI {}, IP {}, TIME {} ",
                dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());

        EndpointHit hit = ehMapper.fromDto(dto);
        hit = repository.save(hit);

        log.info("Added new hit: {}", hit);
        return ehMapper.toDto(hit);
    }

    @Override
    public List<ViewStatsDto> get(ViewStatsRequest request) {
        log.info("Received ViewStatsRequest with params: START {}, END {}, URIS {}, UNIQUE {}",
                request.getStart(), request.getEnd(), request.getUris(), request.getUnique());

        if (request.getStart().isAfter(request.getEnd()) || request.getStart().equals(request.getEnd())) {
            throw new ValidationException("Start or end time is not valid.");
        }

        return getViewStatsByUnique(request).stream()
                .map(vMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ViewStats> getViewStatsByUnique(ViewStatsRequest request) {
        return request.getUnique() ?
                repository.getStatsUniqueIp(request.getStart(), request.getEnd(), request.getUris()) :
                repository.getAllStats(request.getStart(), request.getEnd(), request.getUris());
    }
}
