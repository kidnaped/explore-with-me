package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;
    private final EventRepository eventRepo;

    @Transactional
    @Override
    public CategoryDto register(NewCategoryDto dto) {
        log.info("Received dto with NAME {}", dto.getName());

        Category category = repository.save(mapper.fromDto(dto));
        log.info("Category registered: {}", category);

        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public CategoryDto update(Long catId, NewCategoryDto dto) {
        log.info("Received dto {} and category ID {}", dto, catId);

        Category category = findById(catId);
        category = repository.save(mapper.fromDto(category, dto));
        log.info("Category updated: {}", category);

        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public void deleteById(Long catId) {
        log.info("Received category ID {}", catId);

        Category category = findById(catId);
        if (eventRepo.existsByCategory(category)) {
            throw new IllegalStateException("Category contains undeleted events.");
        }
        repository.deleteById(catId);
        log.info("Category {} deleted.", category);
    }

    @Override
    public Category findById(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + catId + " not found."));
    }
}
