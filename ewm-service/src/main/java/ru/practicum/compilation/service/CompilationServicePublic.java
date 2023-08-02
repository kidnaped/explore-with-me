package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {
    List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);
}
