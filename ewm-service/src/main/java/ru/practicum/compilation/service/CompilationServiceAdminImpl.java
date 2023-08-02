package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Override
    public CompilationDto create(NewCompilationDto dto) {
        log.info("Received DTO {}", dto);
        Compilation compilation = mapper.fromDto(dto);

        addEventsIfExist(compilation, dto);

        compilation = repository.save(compilation);
        log.info("Compilation created: {}, {}", compilation.getId(), compilation.getTitle());

        return mapper.toDto(compilation);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateRequest) {
        Compilation compilation = utils.findById(compId);
        compilation = mapper.fromDto(compilation, updateRequest);

        addEventsIfExist(compilation, updateRequest);

        compilation = repository.save(compilation);
        log.info("Compilation updated: {}, {}", compilation.getId(), compilation.getTitle());

        return mapper.toDto(compilation);
    }

    @Override
    public void deleteById(Long compId) {
        log.info("Received COMP_ID {}", compId);

        Compilation compilation = utils.findById(compId);
        repository.deleteById(compilation.getId());
        log.info("Compilation {} deleted.", compId);
    }

    private <T extends NewCompilationDto> void addEventsIfExist(Compilation compilation, T dto) {
        if (dto.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllByIdIn(dto.getEvents())));
        }
    }
}
