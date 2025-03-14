package com.example.service.impl;

import com.example.domain.Category;
import com.example.dto.CategoryDTO;
import com.example.exception.BusinessException;
import com.example.exception.CategoryNotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.tracing.annotation.NewSpan;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Singleton
@Primary
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @NewSpan("findAllCategories")
    public Flux<CategoryDTO> findAll() {
        log.debug("Finding all categories");
        return categoryRepository.findAll()
                .map(categoryMapper::toDto);
    }

    @Override
    @NewSpan("findCategoryById")
    public Mono<CategoryDTO> findById(UUID id) {
        log.debug("Finding category by id: {}", id);
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException("Category with id " + id + " not found")))
                .map(categoryMapper::toDto);
    }

    @Override
    @NewSpan("findCategoryByName")
    public Mono<CategoryDTO> findByName(String name) {
        log.debug("Finding category by name: {}", name);
        return categoryRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException("Category with name " + name + " not found")))
                .map(categoryMapper::toDto);
    }

    @Override
    @NewSpan("saveCategory")
    public Mono<CategoryDTO> save(CategoryDTO categoryDTO) {
        log.debug("Saving new category: {}", categoryDTO.getName());

        return categoryRepository.findByNameIgnoreCase(categoryDTO.getName())
                .flatMap(existingCategory -> Mono.<Category>error(
                        new BusinessException("Category with name " + categoryDTO.getName() + " already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    Category category = categoryMapper.toEntity(categoryDTO);
                    return categoryRepository.save(category);
                }))
                .map(categoryMapper::toDto);
    }

    @Override
    @NewSpan("updateCategory")
    public Mono<CategoryDTO> update(UUID id, CategoryDTO categoryDTO) {
        log.debug("Updating category with id: {}", id);

        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException("Category with id " + id + " not found")))
                .flatMap(existingCategory -> {
                    // Check if name is being changed and if it already exists
                    if (!existingCategory.getName().equalsIgnoreCase(categoryDTO.getName())) {
                        return categoryRepository.findByNameIgnoreCase(categoryDTO.getName())
                                .flatMap(nameExists -> Mono.<Category>error(
                                        new BusinessException("Category with name " + categoryDTO.getName() + " already exists")))
                                .switchIfEmpty(Mono.defer(() -> {
                                    existingCategory.setName(categoryDTO.getName());
                                    existingCategory.setDescription(categoryDTO.getDescription());
                                    return categoryRepository.save(existingCategory);
                                }));
                    } else {
                        existingCategory.setDescription(categoryDTO.getDescription());
                        return categoryRepository.save(existingCategory);
                    }
                })
                .map(categoryMapper::toDto);
    }

    @Override
    @NewSpan("deleteCategory")
    public Mono<Void> delete(UUID id) {
        log.debug("Deleting category with id: {}", id);
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException("Category with id " + id + " not found")))
                .flatMap(categoryRepository::delete).then();
    }
}
