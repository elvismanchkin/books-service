package com.example.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Controller("/probes")
@Tag(name = "Health Probes", description = "Kubernetes health probes")
public class ProbeController {

    private static final Logger LOG = LoggerFactory.getLogger(ProbeController.class);

    @Get("/liveness")
    @NewSpan("livenessProbe")
    @Operation(summary = "Liveness probe", description = "Checks if the application is running")
    public Mono<HttpResponse<String>> liveness() {
        LOG.debug("Liveness probe check");
        return Mono.just(HttpResponse.ok("Alive"));
    }

    @Get("/readiness")
    @NewSpan("readinessProbe")
    @Operation(summary = "Readiness probe", description = "Checks if the application is ready to receive traffic")
    public Mono<HttpResponse<String>> readiness() {
        LOG.debug("Readiness probe check");
        return Mono.just(HttpResponse.ok("Ready"));
    }
}