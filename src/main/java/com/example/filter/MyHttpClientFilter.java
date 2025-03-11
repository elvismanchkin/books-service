package com.example.filter;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.UUID;

@Filter("/**")
public class MyHttpClientFilter implements HttpClientFilter {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientFilter.class);

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        // Get values from MDC or generate new ones
        String requestId = Optional.ofNullable(MDC.get("requestId"))
                .orElse(UUID.randomUUID().toString());

        String correlationId = Optional.ofNullable(MDC.get("correlationId"))
                .orElse(requestId);

        // Propagate context in request headers
        request.header("X-Request-ID", requestId);
        request.header("X-Correlation-ID", correlationId);

        // Propagate user context if available
        Optional.ofNullable(MDC.get("userId"))
                .ifPresent(value -> request.header("X-User-ID", value));

        Optional.ofNullable(MDC.get("sessionId"))
                .ifPresent(value -> request.header("X-Session-ID", value));

        // Log outgoing request
        LOG.info("Outgoing request: {} {}", request.getMethod(), request.getUri());

        // Continue with the filter chain
        return Flux.from(chain.proceed(request))
                .doOnNext(response ->
                        LOG.info("Received response with status: {}", response.status())
                )
                .doOnError(error ->
                        LOG.error("Error in client request", error)
                );
    }
}