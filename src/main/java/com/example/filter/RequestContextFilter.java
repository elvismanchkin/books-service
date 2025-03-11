package com.example.filter;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Filter("/**")
public class RequestContextFilter implements HttpServerFilter {

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        // Generate or extract request ID and correlation ID
        String requestId = Optional.ofNullable(request.getHeaders().get("X-Request-ID"))
                .orElse(UUID.randomUUID().toString());

        String correlationId = Optional.ofNullable(request.getHeaders().get("X-Correlation-ID"))
                .orElse(requestId);

        // Extract user info if available
        String userId = request.getHeaders().get("X-User-ID");
        String sessionId = request.getHeaders().get("X-Session-ID");

        // Add values to MDC for current thread
        MDC.put("requestId", requestId);
        MDC.put("correlationId", correlationId);

        if (userId != null) {
            MDC.put("userId", userId);
        }

        if (sessionId != null) {
            MDC.put("sessionId", sessionId);
        }

        // Log the incoming request
        log.info("Received request: {} {}", request.getMethod(), request.getPath());

        // Create context for reactive chain
        Context context = Context.empty()
                .put("requestId", requestId)
                .put("correlationId", correlationId);

        if (userId != null) {
            context = context.put("userId", userId);
        }

        if (sessionId != null) {
            context = context.put("sessionId", sessionId);
        }

        try {
            // Continue with the filter chain, propagating context
            Context finalContext = context;
            return Flux.from(chain.proceed(request))
                    .contextWrite(ctx -> ctx.putAll(finalContext))
                    .doOnEach(signal -> {
                        if (signal.isOnComplete() || signal.isOnError()) {
                            MDC.clear();
                        }
                    })
                    .doOnNext(response -> {
                        // Add tracking headers to response
                        response.header("X-Request-ID", requestId);
                        response.header("X-Correlation-ID", correlationId);
                    });
        } catch (Exception e) {
            log.error("Error in request filter", e);
            return Mono.error(e);
        }
    }
}