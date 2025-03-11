package com.example.messaging;

import com.example.event.BookEvent;
import io.micronaut.messaging.annotation.MessageHeader;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.tracing.annotation.NewSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@RabbitListener
public class BookEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(BookEventConsumer.class);

    @NewSpan("consumeBookCreated")
    @Queue("book-created")
    public void receiveBookCreated(BookEvent event, @MessageHeader("X-Correlation-Id") String correlationId) {
        try {
            MDC.put("correlationId", correlationId);
            LOG.info("Received book created event: {}", event.getEventId());
            LOG.debug("Book created: {}", event.getBook().getTitle());
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
            LOG.info("Received book updated event: {}", event.getEventId());
            LOG.debug("Book updated: {}", event.getBook().getTitle());
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
            LOG.info("Received book deleted event: {}", event.getEventId());
            LOG.debug("Book deleted with ID: {}", event.getBook().getId());
            // Process the event
        } finally {
            MDC.remove("correlationId");
        }
    }
}