package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class CompilationServiceUtils {
    private final CompilationRepository repository;

    public Compilation findById(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation " + compId + " not found."));
    }
}
