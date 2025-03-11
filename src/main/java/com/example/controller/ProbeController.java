package com.example.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/probes")
@Tag(name = "Health Probes", description = "Kubernetes health probes")
public class ProbeController {

    @Get("/liveness")
    @NewSpan("livenessProbe")
    @Operation(summary = "Liveness probe", description = "Checks if the application is running")
    public Mono<HttpResponse<String>> liveness() {
        log.debug("Liveness probe check");
        return Mono.just(HttpResponse.ok("Alive"));
    }

    @Get("/readiness")
    @NewSpan("readinessProbe")
    @Operation(summary = "Readiness probe", description = "Checks if the application is ready to receive traffic")
    public Mono<HttpResponse<String>> readiness() {
        log.debug("Readiness probe check");
        return Mono.just(HttpResponse.ok("Ready"));
    }
}