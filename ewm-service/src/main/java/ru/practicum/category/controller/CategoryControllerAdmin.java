package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryServiceAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryServiceAdmin service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto dto,
                              HttpServletRequest request) {
        logForControllers(request);
        return service.register(dto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable Long catId,
                              @Valid @RequestBody NewCategoryDto dto,
                              HttpServletRequest request) {
        logForControllers(request);
        return service.update(catId, dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId,
                       HttpServletRequest request) {
        logForControllers(request);
        service.deleteById(catId);
    }
}
