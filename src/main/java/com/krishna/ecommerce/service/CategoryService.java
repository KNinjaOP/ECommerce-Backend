package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.CategoryRequest;
import com.krishna.ecommerce.dto.CategoryResponse;
import com.krishna.ecommerce.exception.DuplicateResourceException;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.Category;
import com.krishna.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Category already exists with name: " + request.getName()
            );
        }

        Category category = new Category();

        category.setName(request.getName());

        categoryRepository.save(category);

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    public List<CategoryResponse> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();

                    response.setId(category.getId());
                    response.setName(category.getName());

                    return response;
                })
                .toList();
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException(
                    "Category already exists with name: " + request.getName()
            );
        }

        category.setName(request.getName());

        categoryRepository.save(category);

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        categoryRepository.delete(category);
    }
}
