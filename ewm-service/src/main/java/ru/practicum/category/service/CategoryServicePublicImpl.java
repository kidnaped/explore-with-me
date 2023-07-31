package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServicePublicImpl implements CategoryServicePublic {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;
    private final CategoryServiceUtils utils;

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Received parameters FROM {}, SIZE {}", from, size);

        List<Category> categories = repository.findAll(Utils.getPage(from, size)).toList();
        log.info("Found categories: {}.", categories.size());

        return mapper.toDto(categories);
    }

    @Override
    public CategoryDto getById(Long catId) {
        log.info("Received category ID {}", catId);

        Category category = utils.findById(catId);
        log.info("Found category: {}", category);

        return mapper.toDto(category);
    }
}
