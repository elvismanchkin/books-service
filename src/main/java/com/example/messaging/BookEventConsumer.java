package com.example.messaging;

import com.example.event.BookEvent;
import io.micronaut.messaging.annotation.MessageHeader;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.tracing.annotation.NewSpan;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@RabbitListener
public class BookEventConsumer {

    @NewSpan("consumeBookCreated")
    @Queue("book-created")
    public void receiveBookCreated(BookEvent event, @MessageHeader("X-Correlation-Id") String correlationId) {
        try {
            MDC.put("correlationId", correlationId);
            log.info("Received book created event: {}", event.getEventId());
            log.debug("Book created: {}", event.getBook().getTitle());
            // Process the event
        } finally {
            MDC.remove("correlationId");
        }
    }

    @NewSpan("consumeBookUpdated")
    @Queue("book-updated")
    public void receiveBookUpdated(BookEvent event, @MessageHeader("X-Correlation-Id") String correlationId) {
        try {
            MDC.put("correlationId", correlationId);
            log.info("Received book updated event: {}", event.getEventId());
            log.debug("Book updated: {}", event.getBook().getTitle());
            // Process the event
        } finally {
            MDC.remove("correlationId");
        }
    }

    @NewSpan("consumeBookDeleted")
    @Queue("book-deleted")
    public void receiveBookDeleted(BookEvent event, @MessageHeader("X-Correlation-Id") String correlationId) {
        try {
            MDC.put("correlationId", correlationId);
            log.info("Received book deleted event: {}", event.getEventId());
            log.debug("Book deleted with ID: {}", event.getBook().getId());
            // Process the event
        } finally {
            MDC.remove("correlationId");
        }
    }
}