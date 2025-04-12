package com.behalf.store.repo;

import com.behalf.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Add custom queries if needed
}
