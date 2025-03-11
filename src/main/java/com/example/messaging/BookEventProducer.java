package com.example.messaging;

import com.example.dto.BookDTO;
import com.example.event.BookEvent;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.tracing.annotation.NewSpan;

@RabbitClient(value = "book-events")
public interface BookEventProducer {

    @NewSpan("publishBookCreated")
    @Binding("book.created")
    void publishBookCreated(BookEvent event);

    @NewSpan("publishBookUpdated")
    @Binding("book.updated")
    void publishBookUpdated(BookEvent event);

    @NewSpan("publishBookDeleted")
    @Binding("book.deleted")
    void publishBookDeleted(BookEvent event);
}