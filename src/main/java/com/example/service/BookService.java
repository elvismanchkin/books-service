package com.example.service;

import com.example.domain.Book;
import com.example.dto.BookDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BookService {

    Flux<BookDTO> findAll();

    Mono<BookDTO> findById(UUID id);

    Mono<BookDTO> findByIsbn(String isbn);

    Flux<BookDTO> findByTitle(String title);

    Flux<BookDTO> findByAuthor(String author);

    Mono<BookDTO> save(BookDTO bookDTO);

    Mono<BookDTO> update(UUID id, BookDTO bookDTO);

    Mono<Void> delete(UUID id);

    Mono<Book> getBookOrThrow(UUID id);
}