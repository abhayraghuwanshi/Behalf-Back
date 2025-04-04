// StoreServiceImpl Implementation
package com.behalf.delta.service.impl;

import com.behalf.delta.entity.Store;
import com.behalf.delta.entity.StoreOrder;
import com.behalf.delta.entity.ProductInventory;
import com.behalf.delta.exception.ResourceNotFoundException;
import com.behalf.delta.repo.ProductInventoryRepository;
import com.behalf.delta.repo.StoreOrderRepository;
import com.behalf.delta.repo.StoreRepository;
import com.behalf.delta.service.InventoryService;
import com.behalf.delta.service.StoreOrderService;
import com.behalf.delta.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService, StoreOrderService, InventoryService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreOrderRepository orderRepository;

    @Autowired
    private ProductInventoryRepository inventoryRepository;

    // Existing methods implementation
    @Override
    public List<StoreOrder> getOrdersByStatus(String status) {

        return orderRepository.findByStatus(status);
    }

    @Override
    public List<StoreOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public StoreOrder placeOrder(StoreOrder storeOrder) {
        // Implementation details
        return orderRepository.save(storeOrder);
    }

    @Override
    public StoreOrder checkout(StoreOrder storeOrder) {
        // Implementation details
        return orderRepository.save(storeOrder);
    }

    @Override
    public List<StoreOrder> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // New methods implementation
    @Override
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public List<Store> getAllStores(String country) {
        return storeRepository.findAllByCountry(country);
    }

    @Override
    public ProductInventory addInventory(ProductInventory inventory) {
        Store store = storeRepository.findById(inventory.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Attach the managed store entity
        inventory.setStore(store);

        return inventoryRepository.save(inventory); // Now save the inventory item
    }

    @Override
    public ProductInventory updateInventory(ProductInventory inventory) {
        // Check if inventory exists
        inventoryRepository.findById(inventory.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventory.getId()));

        return inventoryRepository.save(inventory);
    }

    @Override
    public List<ProductInventory> getInventoryByProduct() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<ProductInventory> getInventoryByStore(Long storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }
}
