package com.behalf.delta.repo;

import com.behalf.delta.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    List<ProductInventory> findAllById(Long productId);
    List<ProductInventory> findByStoreId(Long storeId);
}