package com.example.config;

import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Slf4j
@Singleton
public class RabbitMQConfig extends ChannelInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Override
    public void initialize(Channel channel, String name) throws IOException {
        log.info("Initializing RabbitMQ channel");

        // Declare exchanges
        channel.exchangeDeclare("book-events", "topic", true);

        // Declare queues
        channel.queueDeclare("book-created", true, false, false, null);
        channel.queueDeclare("book-updated", true, false, false, null);
        channel.queueDeclare("book-deleted", true, false, false, null);

        // Bind queues to exchanges
        channel.queueBind("book-created", "book-events", "book.created");
        channel.queueBind("book-updated", "book-events", "book.updated");
        channel.queueBind("book-deleted", "book-events", "book.deleted");

        log.info("RabbitMQ channel initialized successfully");
    }
}