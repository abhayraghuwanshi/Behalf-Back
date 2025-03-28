package com.behalf.delta.repo;

import com.behalf.delta.entity.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    List<StoreProduct> findByNameContainingAndPriceLessThanEqual(String name, Double price);
}
