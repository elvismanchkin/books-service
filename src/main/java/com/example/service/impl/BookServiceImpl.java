package com.example.service.impl;

import com.example.domain.Book;
import com.example.dto.BookDTO;
import com.example.exception.BookNotFoundException;
import com.example.exception.BusinessException;
import com.example.mapper.BookMapper;
import com.example.repository.BookRepository;
import com.example.service.BookService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.tracing.annotation.NewSpan;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Singleton
@Primary
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @NewSpan("findAllBooks")
    @Override
    public Flux<BookDTO> findAll() {
        log.debug("Finding all books");
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @NewSpan("findBookById")
    @Override
    public Mono<BookDTO> findById(UUID id) {
        log.debug("Finding book by id: {}", id);
        return getBookOrThrow(id).map(bookMapper::toDto);
    }

    @NewSpan("findBookByIsbn")
    @Override
    public Mono<BookDTO> findByIsbn(String isbn) {
        log.debug("Finding book by ISBN: {}", isbn);
        return bookRepository
                .findByIsbn(isbn)
                .switchIfEmpty(Mono.error(() -> new BookNotFoundException("Book with ISBN " + isbn + " not found")))
                .map(bookMapper::toDto);
    }

    @Override
    @NewSpan("findBooksByTitle")
    public Flux<BookDTO> findByTitle(String title) {
        log.debug("Finding books by title containing: {}", title);
        return bookRepository.findByTitleContainsIgnoreCase(title).map(bookMapper::toDto);
    }

    @Override
    @NewSpan("findBooksByAuthor")
    public Flux<BookDTO> findByAuthor(String author) {
        log.debug("Finding books by author containing: {}", author);
        return bookRepository.findByAuthorContainsIgnoreCase(author).map(bookMapper::toDto);
    }

    @Override
    @NewSpan("saveBook")
    public Mono<BookDTO> save(BookDTO bookDTO) {
        log.debug("Saving new book: {}", bookDTO.getTitle());

        // Check if book with ISBN already exists
        return bookRepository
                .findByIsbn(bookDTO.getIsbn())
                .flatMap(existingBook -> Mono.<Book>error(
                        new BusinessException("Book with ISBN " + bookDTO.getIsbn() + " already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    Book book = bookMapper.toEntity(bookDTO);
                    return bookRepository.save(book);
                }))
                .map(bookMapper::toDto);
    }

    @Override
    @NewSpan("updateBook")
    public Mono<BookDTO> update(UUID id, BookDTO bookDTO) {
        log.debug("Updating book with id: {}", id);

        return getBookOrThrow(id)
                .flatMap(existingBook -> {
                    // Update only allowed fields
                    existingBook.setTitle(bookDTO.getTitle());
                    existingBook.setAuthor(bookDTO.getAuthor());
                    existingBook.setDescription(bookDTO.getDescription());

                    // Don't allow ISBN change
                    if (!existingBook.getIsbn().equals(bookDTO.getIsbn())) {
                        return Mono.error(new BusinessException("ISBN cannot be changed"));
                    }

                    return bookRepository.update(existingBook);
                })
                .map(bookMapper::toDto);
    }

    @Override
    @NewSpan("deleteBook")
    public Mono<Void> delete(UUID id) {
        log.debug("Deleting book with id: {}", id);
        return getBookOrThrow(id).flatMap(bookRepository::delete).then();
    }

    @Override
    public Mono<Book> getBookOrThrow(UUID id) {
        return bookRepository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> new BookNotFoundException("Book with id " + id + " not found")));
    }
}
