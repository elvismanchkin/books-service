package com.example.controller;

import com.example.dto.BookDTO;
import com.example.event.BookEvent;
import com.example.messaging.BookEventProducer;
import com.example.service.BookService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller("/books")
@Validated
@Tag(name = "Books", description = "Book management endpoints")
public class BookController {

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final BookEventProducer bookEventProducer;

    public BookController(BookService bookService, BookEventProducer bookEventProducer) {
        this.bookService = bookService;
        this.bookEventProducer = bookEventProducer;
    }

    @Get
    @NewSpan("getAllBooks")
    @Operation(summary = "Get all books", description = "Retrieves all books in the system")
    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully")
    public Flux<BookDTO> getAllBooks() {
        LOG.info("Request to get all books");
        return bookService.findAll();
    }

    @Get("/{id}")
    @NewSpan("getBookById")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Book retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public Mono<BookDTO> getBookById(@PathVariable UUID id) {
        LOG.info("Request to get book by ID: {}", id);
        return bookService.findById(id);
    }

    @Get("/isbn/{isbn}")
    @NewSpan("getBookByIsbn")
    @Operation(summary = "Get book by ISBN", description = "Retrieves a book by its ISBN")
    @ApiResponse(responseCode = "200", description = "Book retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public Mono<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        LOG.info("Request to get book by ISBN: {}", isbn);
        return bookService.findByIsbn(isbn);
    }

    @Get("/search")
    @NewSpan("searchBooks")
    @Operation(summary = "Search books", description = "Search books by title or author")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public Flux<BookDTO> searchBooks(
            @Parameter(description = "Title to search for") @QueryValue(value = "title", defaultValue = "") String title,
            @Parameter(description = "Author to search for") @QueryValue(value = "author", defaultValue = "") String author) {

        LOG.info("Request to search books with title: '{}', author: '{}'", title, author);

        if (!title.isEmpty()) {
            return bookService.findByTitle(title);
        } else if (!author.isEmpty()) {
            return bookService.findByAuthor(author);
        } else {
            return bookService.findAll();
        }
    }

    @Post
    @NewSpan("createBook")
    @Operation(summary = "Create new book", description = "Creates a new book in the system")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @ApiResponse(
            responseCode = "475",
            description = "Business validation error",
            content = @Content(schema = @Schema(implementation = org.zalando.problem.Problem.class))
    )
    public Mono<HttpResponse<BookDTO>> createBook(@Valid @Body BookDTO bookDTO) {
        LOG.info("Request to create new book: {}", bookDTO.getTitle());

        return bookService.save(bookDTO)
                .map(savedBook -> {
                    // Publish event
                    bookEventProducer.publishBookCreated(BookEvent.created(savedBook));
                    return HttpResponse.created(savedBook);
                });
    }

    @Put("/{id}")
    @NewSpan("updateBook")
    @Operation(summary = "Update book", description = "Updates an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(
            responseCode = "475",
            description = "Business validation error",
            content = @Content(schema = @Schema(implementation = org.zalando.problem.Problem.class))
    )
    public Mono<BookDTO> updateBook(@PathVariable UUID id, @Valid @Body BookDTO bookDTO) {
        LOG.info("Request to update book with ID: {}", id);

        return bookService.update(id, bookDTO)
                .doOnSuccess(updatedBook ->
                        bookEventProducer.publishBookUpdated(BookEvent.updated(updatedBook))
                );
    }

    @Delete("/{id}")
    @NewSpan("deleteBook")
    @Operation(summary = "Delete book", description = "Deletes a book from the system")
    @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public Mono<HttpResponse<?>> deleteBook(@PathVariable UUID id) {
        LOG.info("Request to delete book with ID: {}", id);

        return bookService.findById(id)
                .flatMap(book -> {
                    bookEventProducer.publishBookDeleted(BookEvent.deleted(book));
                    return bookService.delete(id).thenReturn(HttpResponse.noContent());
                });
    }
}
