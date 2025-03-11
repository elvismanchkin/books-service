package com.example.service;

import com.example.dto.CategoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryService {

    Flux<CategoryDTO> findAll();

    Mono<CategoryDTO> findById(UUID id);

    Mono<CategoryDTO> findByName(String name);

    Mono<CategoryDTO> save(CategoryDTO categoryDTO);

    Mono<CategoryDTO> update(UUID id, CategoryDTO categoryDTO);

    Mono<Void> delete(UUID id);
}