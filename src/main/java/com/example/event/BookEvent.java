package com.example.event;

import com.example.dto.BookDTO;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class BookEvent {

    @Builder.Default
    private UUID eventId = UUID.randomUUID();

    private EventType eventType;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private BookDTO book;

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }

    public static BookEvent created(BookDTO book) {
        return BookEvent.builder()
                .eventType(EventType.CREATED)
                .book(book)
                .build();
    }

    public static BookEvent updated(BookDTO book) {
        return BookEvent.builder()
                .eventType(EventType.UPDATED)
                .book(book)
                .build();
    }

    public static BookEvent deleted(BookDTO book) {
        return BookEvent.builder()
                .eventType(EventType.DELETED)
                .book(book)
                .build();
    }
}
