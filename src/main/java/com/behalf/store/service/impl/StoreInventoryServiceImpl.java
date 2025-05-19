package com.behalf.store.service.impl;

import com.behalf.delta.exception.ResourceNotFoundException;
import com.behalf.store.model.ProductInventory;
import com.behalf.store.model.Store;
import com.behalf.store.repo.ProductInventoryRepository;
import com.behalf.store.repo.StoreRepository;
import com.behalf.store.service.StoreInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreInventoryServiceImpl implements StoreInventoryService {

    @Autowired
    private ProductInventoryRepository inventoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public ProductInventory addInventory(ProductInventory inventory) {
        Store store = storeRepository.findById(inventory.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Store not found"));
        inventory.setStore(store);
        return inventoryRepository.save(inventory);
    }

    @Override
    public ProductInventory updateInventory(ProductInventory inventory) {
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
