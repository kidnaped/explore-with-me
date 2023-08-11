package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class CategoryServiceUtils {
    private final CategoryRepository repository;

    public Category findById(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + catId + " not found."));
    }
}
