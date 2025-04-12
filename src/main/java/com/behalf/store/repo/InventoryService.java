package com.behalf.store.repo;

import com.behalf.store.model.ProductInventory;

import java.util.List;

public interface InventoryService {
    ProductInventory addInventory(ProductInventory inventory);
    ProductInventory updateInventory(ProductInventory inventory);
    List<ProductInventory> getInventoryByProduct();
    List<ProductInventory> getInventoryByStore(Long storeId);
}
