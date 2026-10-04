package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.CategoryRequest;
import com.krishna.ecommerce.dto.CategoryResponse;
import com.krishna.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Categories",
        description = "APIs for managing product categories"
)
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create a category",
            description = "Creates a new product category. This endpoint is restricted to administrators."
    )
    @PostMapping
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.createCategory(request);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Returns a product category using its ID."
    )
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @Operation(
            summary = "Get all categories",
            description = "Returns a list of all product categories."
    )
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(
            summary = "Update a category",
            description = "Updates an existing product category. This endpoint is restricted to administrators."
    )
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.updateCategory(id, request);
    }

    @Operation(
            summary = "Delete a category",
            description = "Deletes an existing product category. This endpoint is restricted to administrators."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}