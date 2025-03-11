package com.example.repository;

import com.example.domain.Category;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface CategoryRepository extends ReactorCrudRepository<Category, UUID> {

    Mono<Category> findByNameIgnoreCase(String name);
}