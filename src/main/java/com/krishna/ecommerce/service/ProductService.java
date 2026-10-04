package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.ProductRequest;
import com.krishna.ecommerce.dto.ProductResponse;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.Category;
import com.krishna.ecommerce.model.Product;
import com.krishna.ecommerce.repository.CategoryRepository;
import com.krishna.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + request.getCategoryId()
                        ));

        Product product = new Product();

        product.setName(request.getName());
        product.setCategory(category);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        productRepository.save(product);

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setCategoryId(category.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());

        return response;
    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ));

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setCategoryId(product.getCategory().getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());

        return response;
    }

    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> {
                    ProductResponse response = new ProductResponse();

                    response.setId(product.getId());
                    response.setCategoryId(product.getCategory().getId());
                    response.setName(product.getName());
                    response.setDescription(product.getDescription());
                    response.setPrice(product.getPrice());
                    response.setStock(product.getStock());

                    return response;
                })
                .toList();
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + request.getCategoryId()
                        )
                );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        productRepository.save(product);

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategoryId(category.getId());

        return response;
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        productRepository.delete(product);
    }
}
