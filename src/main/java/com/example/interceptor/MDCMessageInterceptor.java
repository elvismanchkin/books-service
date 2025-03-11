package com.example.interceptor;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.context.annotation.Type;
import io.micronaut.messaging.annotation.MessageMapping;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.rabbitmq.intercept.RabbitMQIntroductionAdvice;
import io.micronaut.rabbitmq.intercept.MutableBasicProperties;
import jakarta.inject.Singleton;
import org.slf4j.MDC;

import java.util.Map;
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

            // Add a hook to include MDC values in message headers
            context.setAttribute(
                    RabbitMQIntroductionAdvice.PROPERTY_MAPPER,
                    (MutableBasicProperties properties) -> {
                        Map<String, Object> headers = properties.getHeaders();
                        headers.put("X-Correlation-Id", correlationId);

                        // Add other MDC values that should be propagated
                        Optional.ofNullable(MDC.get("requestId")).ifPresent(v -> headers.put("X-Request-Id", v));
                        Optional.ofNullable(MDC.get("userId")).ifPresent(v -> headers.put("X-User-Id", v));
                        Optional.ofNullable(MDC.get("sessionId")).ifPresent(v -> headers.put("X-Session-Id", v));

                        return properties;
                    }
            );
        }

        return delegate.intercept(context);
    }
}