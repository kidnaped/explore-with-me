package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final CompilationServiceUtils utils;

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Received PINNED {}, FROM {}, SIZE {}", pinned, from, size);

        List<Compilation> compilations = repository.findAllByPinned(pinned, Utils.getPage(from, size));
        log.info("Found {} compilations.", compilations.size());

        return mapper.toDto(compilations);
    }

    @Override
    public CompilationDto getById(Long compId) {
        log.info("Received COMP_ID {}", compId);

        Compilation compilation = utils.findById(compId);
        log.info("Compilation found: {} {}", compilation.getId(), compilation.getTitle());

        return mapper.toDto(compilation);
    }
}
