package com.example.interceptor;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.context.annotation.Type;
import io.micronaut.messaging.annotation.MessageMapping;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.rabbitmq.intercept.RabbitMQIntroductionAdvice;
import jakarta.inject.Singleton;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.UUID;

@Singleton
@Type(RabbitClient.class)
public class MDCMessageInterceptor implements MethodInterceptor<Object, Object> {

    private final RabbitMQIntroductionAdvice delegate;

    public MDCMessageInterceptor(RabbitMQIntroductionAdvice delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        if (context.hasAnnotation(MessageMapping.class)) {
            // Get current correlation ID from MDC or generate a new one
            String correlationId = Optional.ofNullable(MDC.get("correlationId"))
                    .orElse(UUID.randomUUID().toString());

            // Create a custom attribute for our headers
            context.setAttribute("mdc.headers", true);
            context.setAttribute("mdc.correlationId", correlationId);
            context.setAttribute("mdc.requestId", MDC.get("requestId"));
            context.setAttribute("mdc.userId", MDC.get("userId"));
            context.setAttribute("mdc.sessionId", MDC.get("sessionId"));

            // You'll need to create a custom RabbitMQ client factory that reads these attributes
            // and applies them to the message headers
        }

        return delegate.intercept(context);
    }
}