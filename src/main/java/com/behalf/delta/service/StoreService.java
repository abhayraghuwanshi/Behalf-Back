package com.behalf.delta.service;

import com.behalf.delta.entity.Store;
import com.behalf.delta.entity.StoreOrder;
import com.behalf.delta.entity.ProductInventory;
import java.util.List;

public interface StoreService {
    // Order methods
    List<StoreOrder> getOrdersByStatus(String status);
    List<StoreOrder> getAllOrders();
    StoreOrder placeOrder(StoreOrder storeOrder);
    StoreOrder checkout(StoreOrder storeOrder);
    List<StoreOrder> getOrdersByUser(Long userId);

    // Store methods
    Store createStore(Store store);
    Store getStoreById(Long storeId);
    List<Store> getAllStores();

    // Inventory methods
    ProductInventory addInventory(ProductInventory inventory);
    ProductInventory updateInventory(ProductInventory inventory);
    List<ProductInventory> getInventoryByProduct(Long productId);
    List<ProductInventory> getInventoryByStore(Long storeId);
}
