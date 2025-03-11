package com.example.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.net.URI;

@Slf4j
@Singleton
@Requires(classes = {Throwable.class, ExceptionHandler.class})
public class GlobalExceptionHandler implements ExceptionHandler<Throwable, HttpResponse<?>> {

    @Override
    @Produces("application/problem+json")
    public HttpResponse<?> handle(HttpRequest request, Throwable exception) {
        log.error("Exception caught by global exception handler", exception);

        if (exception instanceof BookNotFoundException || exception instanceof CategoryNotFoundException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND)
                    .body(buildProblem(Status.NOT_FOUND, exception.getMessage()));
        } else if (exception instanceof BusinessException) {
            // Custom status code 475 for business errors
            return HttpResponse.status(475, "BUSINESS_ERROR")
                    .body(buildProblem(Status.valueOf(475), exception.getMessage()));
        } else if (exception instanceof ConstraintViolationException) {
            return HttpResponse.badRequest()
                    .body(buildProblem(Status.BAD_REQUEST, "Validation error: " + exception.getMessage()));
        } else {
            return HttpResponse.serverError()
                    .body(buildProblem(Status.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
        }
    }

    private ThrowableProblem buildProblem(Status status, String detail) {
        return Problem.builder()
                .withType(URI.create("https://example.com/problems/" + status.getStatusCode()))
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(detail)
                .build();
    }
}