package com.example.dto;

    import io.micronaut.core.annotation.Introspected;
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
@Introspected
@Serdeable
public class BookDTO {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String author;

    @Size(max = 2000)
    private String description;

    @NotNull
    private String isbn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    private Set<CategoryDTO> categories = new HashSet<>();
}
