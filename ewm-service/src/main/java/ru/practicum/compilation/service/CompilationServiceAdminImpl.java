package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.repository.EventRepository;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final CompilationServiceUtils utils;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto dto) {
        log.info("Received DTO {}", dto.getTitle());

        Compilation compilation = mapper.fromDto(dto);

        if (dto.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllByIdIn(dto.getEvents())));
        }

        compilation = repository.save(compilation);
        log.info("Compilation created: {}, {}", compilation.getId(), compilation.getTitle());

        return mapper.toDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateRequest) {
        log.info("Received COMP_ID {}, UPDATE_REQUEST {}", compId, updateRequest.getTitle());

        Compilation compilation = utils.findById(compId);
        mapper.fromDto(compilation, updateRequest);

        if (updateRequest.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllByIdIn(updateRequest.getEvents())));
        }

        compilation = repository.save(compilation);
        log.info("Compilation updated: {}, {}", compilation.getId(), compilation.getTitle());

        return mapper.toDto(compilation);
    }

    @Transactional
    @Override
    public void deleteById(Long compId) {
        log.info("Received COMP_ID {}", compId);

        Compilation compilation = utils.findById(compId);
        repository.deleteById(compilation.getId());
        log.info("Compilation {} deleted.", compId);
    }
}
