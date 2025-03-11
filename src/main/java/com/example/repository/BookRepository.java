package com.example.repository;

import com.example.domain.Book;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface BookRepository extends ReactorCrudRepository<Book, UUID> {

    Flux<Book> findByTitleContainsIgnoreCase(String title);

    Flux<Book> findByAuthorContainsIgnoreCase(String author);

    Mono<Book> findByIsbn(String isbn);
}