package com.example.domain;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
@MappedEntity("books")
public class Book {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    @NotBlank
    @Size(max = 255)
    private String title;

    @NonNull
    @NotBlank
    @Size(max = 255)
    private String author;

    @Size(max = 2000)
    private String description;

    @NonNull
    @NotNull
    private String isbn;

    @DateCreated
    private LocalDateTime createdAt;

    @DateUpdated
    private LocalDateTime updatedAt;

    @Builder.Default
    @Relation(value = Relation.Kind.MANY_TO_MANY)
    private Set<Category> categories = new HashSet<>();
}