//package com.example.controller;
//
//import com.example.dto.CategoryDTO;
//import com.example.service.CategoryService;
//import io.micronaut.http.HttpResponse;
//import io.micronaut.http.annotation.Body;
//import io.micronaut.http.annotation.Controller;
//import io.micronaut.http.annotation.Delete;
//import io.micronaut.http.annotation.Get;
//import io.micronaut.http.annotation.PathVariable;
//import io.micronaut.http.annotation.Post;
//import io.micronaut.http.annotation.Put;
//import io.micronaut.tracing.annotation.NewSpan;
//import io.micronaut.validation.Validated;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//@Slf4j
//@RequiredArgsConstructor
//@Controller("/categories")
//@Validated
//@Tag(name = "Categories", description = "Category management endpoints")
//public class CategoryController {
//
//    private final CategoryService categoryService;
//
//    @Get
//    @NewSpan("getAllCategories")
//    @Operation(summary = "Get all categories", description = "Retrieves all categories in the system")
//    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
//    public Flux<CategoryDTO> getAllCategories() {
//        log.info("Request to get all categories");
//        return categoryService.findAll();
//    }
//
//    @Get("/{id}")
//    @NewSpan("getCategoryById")
//    @Operation(summary = "Get category by ID", description = "Retrieves a category by its unique identifier")
//    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
//    @ApiResponse(responseCode = "404", description = "Category not found")
//    public Mono<CategoryDTO> getCategoryById(@PathVariable UUID id) {
//        log.info("Request to get category by ID: {}", id);
//        return categoryService.findById(id);
//    }
//
//    @Get("/name/{name}")
//    @NewSpan("getCategoryByName")
//    @Operation(summary = "Get category by name", description = "Retrieves a category by its name")
//    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
//    @ApiResponse(responseCode = "404", description = "Category not found")
//    public Mono<CategoryDTO> getCategoryByName(@PathVariable String name) {
//        log.info("Request to get category by name: {}", name);
//        return categoryService.findByName(name);
//    }
//
//    @Post
//    @NewSpan("createCategory")
//    @Operation(summary = "Create new category", description = "Creates a new category in the system")
//    @ApiResponse(responseCode = "201", description = "Category created successfully")
//    @ApiResponse(
//            responseCode = "475",
//            description = "Business validation error",
//            content = @Content(schema = @Schema(implementation = org.zalando.problem.Problem.class))
//    )
//    public Mono<HttpResponse<CategoryDTO>> createCategory(@Valid @Body CategoryDTO categoryDTO) {
//        log.info("Request to create new category: {}", categoryDTO.getName());
//        return categoryService.save(categoryDTO)
//                .map(HttpResponse::created);
//    }
//
//    @Put("/{id}")
//    @NewSpan("updateCategory")
//    @Operation(summary = "Update category", description = "Updates an existing category")
//    @ApiResponse(responseCode = "200", description = "Category updated successfully")
//    @ApiResponse(responseCode = "404", description = "Category not found")
//    @ApiResponse(
//            responseCode = "475",
//            description = "Business validation error",
//            content = @Content(schema = @Schema(implementation = org.zalando.problem.Problem.class))
//    )
//    public Mono<CategoryDTO> updateCategory(@PathVariable UUID id, @Valid @Body CategoryDTO categoryDTO) {
//        log.info("Request to update category with ID: {}", id);
//        return categoryService.update(id, categoryDTO);
//    }
//
//    @Delete("/{id}")
//    @NewSpan("deleteCategory")
//    @Operation(summary = "Delete category", description = "Deletes a category from the system")
//    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
//    @ApiResponse(responseCode = "404", description = "Category not found")
//    public Mono<HttpResponse<?>> deleteCategory(@PathVariable UUID id) {
//        log.info("Request to delete category with ID: {}", id);
//        return categoryService.delete(id)
//                .thenReturn(HttpResponse.noContent());
//    }
//}