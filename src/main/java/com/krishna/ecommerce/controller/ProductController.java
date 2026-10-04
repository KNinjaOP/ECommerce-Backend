package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.ProductRequest;
import com.krishna.ecommerce.dto.ProductResponse;
import com.krishna.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Products",
        description = "APIs for managing products"
)
@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create a product",
            description = "Creates a new product. This endpoint is restricted to administrators."
    )
    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {

        return productService.createProduct(request);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Returns a specific product using its ID."
    )
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @Operation(
            summary = "Get all products",
            description = "Returns a list of all products available in the store."
    )
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(
            summary = "Update a product",
            description = "Updates an existing product. This endpoint is restricted to administrators."
    )
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        return productService.updateProduct(id, request);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes an existing product. This endpoint is restricted to administrators."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}