package com.behalf.store.service;

import com.behalf.store.model.ProductInventory;

import java.util.List;

public interface StoreInventoryService {
    ProductInventory addInventory(ProductInventory inventory);
    ProductInventory updateInventory(ProductInventory inventory);
    List<ProductInventory> getInventoryByProduct();
    List<ProductInventory> getInventoryByStore(Long storeId);
}
