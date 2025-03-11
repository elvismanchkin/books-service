package com.example.domain;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.annotation.sql.JoinColumn;
import io.micronaut.data.annotation.sql.JoinTable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Serdeable
@MappedEntity("books")
public class Book {

    @Id
    @GeneratedValue
    @MappedProperty(value = "id")
    private UUID id;

    @NonNull
    @NotBlank
    @Size(max = 255)
    @MappedProperty(value = "title")
    private String title;

    @NonNull
    @NotBlank
    @Size(max = 255)
    @MappedProperty(value = "author")
    private String author;

    @Size(max = 2000)
    @MappedProperty(value = "description")
    private String description;

    @NonNull
    @NotNull
    @MappedProperty(value = "isbn")
    private String isbn;

    @DateCreated
    @MappedProperty(value = "created_at")
    private LocalDateTime createdAt;

    @DateUpdated
    @MappedProperty(value = "updated_at")
    private LocalDateTime updatedAt;

    @Relation(value = Relation.Kind.MANY_TO_MANY)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Book(UUID id, @NonNull @NotBlank @Size(max = 255) String title, @NonNull @NotBlank @Size(max = 255) String author, @Size(max = 2000) String description, @NonNull @NotNull String isbn, LocalDateTime createdAt, LocalDateTime updatedAt, Set<Category> categories) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categories = categories;
    }

    public Book() {
    }

    private static Set<Category> $default$categories() {
        return new HashSet<>();
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public UUID getId() {
        return this.id;
    }

    public @NonNull @NotBlank @Size(max = 255) String getTitle() {
        return this.title;
    }

    public @NonNull @NotBlank @Size(max = 255) String getAuthor() {
        return this.author;
    }

    public @Size(max = 2000) String getDescription() {
        return this.description;
    }

    public @NonNull @NotNull String getIsbn() {
        return this.isbn;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(@NonNull @NotBlank @Size(max = 255) String title) {
        this.title = title;
    }

    public void setAuthor(@NonNull @NotBlank @Size(max = 255) String author) {
        this.author = author;
    }

    public void setDescription(@Size(max = 2000) String description) {
        this.description = description;
    }

    public void setIsbn(@NonNull @NotNull String isbn) {
        this.isbn = isbn;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Book)) return false;
        final Book other = (Book) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$isbn = this.getIsbn();
        final Object other$isbn = other.getIsbn();
        if (this$isbn == null ? other$isbn != null : !this$isbn.equals(other$isbn)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$updatedAt = this.getUpdatedAt();
        final Object other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !this$updatedAt.equals(other$updatedAt)) return false;
        final Object this$categories = this.getCategories();
        final Object other$categories = other.getCategories();
        if (this$categories == null ? other$categories != null : !this$categories.equals(other$categories))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Book;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $author = this.getAuthor();
        result = result * PRIME + ($author == null ? 43 : $author.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $isbn = this.getIsbn();
        result = result * PRIME + ($isbn == null ? 43 : $isbn.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        final Object $updatedAt = this.getUpdatedAt();
        result = result * PRIME + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        final Object $categories = this.getCategories();
        result = result * PRIME + ($categories == null ? 43 : $categories.hashCode());
        return result;
    }

    public String toString() {
        return "Book(id=" + this.getId() + ", title=" + this.getTitle() + ", author=" + this.getAuthor() + ", description=" + this.getDescription() + ", isbn=" + this.getIsbn() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", categories=" + this.getCategories() + ")";
    }

    public static class BookBuilder {
        private UUID id;
        private @NonNull
        @NotBlank
        @Size(max = 255) String title;
        private @NonNull
        @NotBlank
        @Size(max = 255) String author;
        private @Size(max = 2000) String description;
        private @NonNull
        @NotNull String isbn;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Set<Category> categories$value;
        private boolean categories$set;

        BookBuilder() {
        }

        public BookBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BookBuilder title(@NonNull @NotBlank @Size(max = 255) String title) {
            this.title = title;
            return this;
        }

        public BookBuilder author(@NonNull @NotBlank @Size(max = 255) String author) {
            this.author = author;
            return this;
        }

        public BookBuilder description(@Size(max = 2000) String description) {
            this.description = description;
            return this;
        }

        public BookBuilder isbn(@NonNull @NotNull String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BookBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BookBuilder categories(Set<Category> categories) {
            this.categories$value = categories;
            this.categories$set = true;
            return this;
        }

        public Book build() {
            Set<Category> categories$value = this.categories$value;
            if (!this.categories$set) {
                categories$value = Book.$default$categories();
            }
            return new Book(this.id, this.title, this.author, this.description, this.isbn, this.createdAt, this.updatedAt, categories$value);
        }

        public String toString() {
            return "Book.BookBuilder(id=" + this.id + ", title=" + this.title + ", author=" + this.author + ", description=" + this.description + ", isbn=" + this.isbn + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + ", categories$value=" + this.categories$value + ")";
        }
    }
}