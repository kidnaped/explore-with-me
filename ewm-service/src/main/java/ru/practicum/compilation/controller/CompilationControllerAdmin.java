package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationServiceAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationControllerAdmin {
    private final CompilationServiceAdmin service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto,
                                 HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.create(dto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Valid @RequestBody UpdateCompilationRequest updateRequest,
                                 HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.update(compId, updateRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId,
                       HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        service.deleteById(compId);
    }
}
