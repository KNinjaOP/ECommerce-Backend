package com.krishna.ecommerce.repository;

import com.krishna.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}