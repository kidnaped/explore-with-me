package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryServicePublic {
    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);

    Category findById(Long catId);
}
