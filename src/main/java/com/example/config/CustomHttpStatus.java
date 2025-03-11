package com.example.config;

import io.micronaut.http.HttpStatus;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class CustomHttpStatus {

    @PostConstruct
    public void init() {
        try {
            // Register custom status 475 BUSINESS_ERROR
            registerHttpStatus(475, "BUSINESS_ERROR");

            // Register custom status in Zalando Problem library
            registerProblemStatus(475, "Business Error");

        } catch (Exception e) {
            throw new RuntimeException("Failed to register custom HTTP status", e);
        }
    }

    private void registerHttpStatus(int code, String reason) throws Exception {
        Field valuesField = HttpStatus.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Integer, HttpStatus> values = (Map<Integer, HttpStatus>) valuesField.get(null);

        HttpStatus status = HttpStatus.valueOf(code);
        values.put(code, status);
    }

    private void registerProblemStatus(int statusCode, String reasonPhrase) throws Exception {
        // Get the VALUES field from the Status enum
        Field valuesField = Status.class.getDeclaredField("VALUES");
        valuesField.setAccessible(true);

        // Get the current values map
        @SuppressWarnings("unchecked")
        Map<Integer, Status> values = (Map<Integer, Status>) valuesField.get(null);

        // If the map is unmodifiable, we need to create a new one
        Map<Integer, StatusType> newValues = new HashMap<>(values);

        // Create a new Status field via reflection
        Field statusCodeField = Status.class.getDeclaredField("statusCode");
        Field reasonPhraseField = Status.class.getDeclaredField("reasonPhrase");
        statusCodeField.setAccessible(true);
        reasonPhraseField.setAccessible(true);

        // Create a new Status enum instance via sun.misc.Unsafe or similar advanced technique
        // For simplicity in this example, we'll just create a wrapper
        StatusType newStatus = new StatusType() {
            @Override
            public int getStatusCode() {
                return statusCode;
            }

            @Override
            public String getReasonPhrase() {
                return reasonPhrase;
            }
        };

        newValues.put(statusCode, newStatus);

        // Replace the VALUES field with our new map
        valuesField.set(null, newValues);
    }
}