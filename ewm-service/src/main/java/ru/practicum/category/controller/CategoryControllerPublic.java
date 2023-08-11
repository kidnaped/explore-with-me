package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryServicePublic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryControllerPublic {
    private final CategoryServicePublic service;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        logForControllers(request);
        return service.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId,
                               HttpServletRequest request) {
        logForControllers(request);
        return service.getById(catId);
    }
}
