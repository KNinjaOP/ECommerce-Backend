package com.krishna.ecommerce.repository;

import com.krishna.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
