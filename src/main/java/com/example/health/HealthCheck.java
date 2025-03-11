package com.example.health;

import io.micronaut.health.HealthStatus;
import io.micronaut.management.health.indicator.HealthIndicator;
import io.micronaut.management.health.indicator.HealthResult;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Singleton
public class HealthCheck implements HealthIndicator {

    @Override
    public Publisher<HealthResult> getResult() {
        return Mono.just(HealthResult.builder("application")
                .status(HealthStatus.UP)
                .details(Collections.singletonMap("version", "0.1.0"))
                .build());
    }
}