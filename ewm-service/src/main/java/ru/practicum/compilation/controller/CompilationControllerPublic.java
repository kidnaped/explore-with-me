package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationServicePublic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPublic {
    private final CompilationServicePublic service;

    @GetMapping
    public List<CompilationDto> findCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.findCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable Long compId,
                                  HttpServletRequest servletRequest) {
        logForControllers(servletRequest);
        return service.getById(compId);
    }
}
