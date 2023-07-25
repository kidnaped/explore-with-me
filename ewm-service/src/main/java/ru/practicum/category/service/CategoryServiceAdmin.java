package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

public interface CategoryServiceAdmin {
    CategoryDto register(NewCategoryDto dto);

    CategoryDto update(Long catId, NewCategoryDto dto);

    void deleteById(Long id);

    Category findById(Long catId);
}
