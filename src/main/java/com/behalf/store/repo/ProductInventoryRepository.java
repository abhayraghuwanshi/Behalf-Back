package com.behalf.store.repo;

import com.behalf.store.model.Product;
import com.behalf.store.model.ProductInventory;
import com.behalf.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    List<ProductInventory> findAllById(Long productId);
    List<ProductInventory> findByStoreId(Long storeId);
    List<ProductInventory> findByStoreIdIn(List<Long> storeIds);

    List<ProductInventory> findByStoreIdInAndProductId(List<Long> storeIds, Long productId);

}