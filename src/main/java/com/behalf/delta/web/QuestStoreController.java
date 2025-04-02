package com.behalf.delta.web;

import com.behalf.delta.service.StoreService;
import com.behalf.delta.entity.Store;
import com.behalf.delta.entity.StoreOrder;
import com.behalf.delta.entity.ProductInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/store")
public class QuestStoreController {

    @Autowired
    private StoreService storeService;

    // Get all orders or filter by status (e.g., "OUT_OF_STOCK")
    @GetMapping("/orders")
    public List<StoreOrder> getOrders(@RequestParam(required = false) String status) {
        if(status != null) {
            return storeService.getOrdersByStatus(status);
        }
        return storeService.getAllOrders();
    }

    // Place an order
    @PostMapping("/orders")
    public StoreOrder placeOrder(@RequestBody StoreOrder storeOrder) {
        return storeService.placeOrder(storeOrder);
    }

    // Checkout – add additional information such as address, price, etc.
    @PostMapping("/checkout")
    public StoreOrder checkout(@RequestBody StoreOrder storeOrder) {
        return storeService.checkout(storeOrder);
    }

    // Display orders for a specific user
    @GetMapping("/myorders")
    public List<StoreOrder> getMyOrders(@RequestParam Long userId) {
        return storeService.getOrdersByUser(userId);
    }

    // NEW ENDPOINTS

    // Create a new store - restricted to admin users
    @PostMapping("/stores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        Store createdStore = storeService.createStore(store);
        return ResponseEntity.ok(createdStore);
    }

    // Get all stores
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    // Get a specific store by ID
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    // Add inventory for a product at a specific store location
    @PostMapping("/inventory")
    @PreAuthorize("hasRole('STORE_MANAGER')")
    public ResponseEntity<ProductInventory> addInventory(@RequestBody ProductInventory inventory) {
        ProductInventory addedInventory = storeService.addInventory(inventory);
        return ResponseEntity.ok(addedInventory);
    }

    // Update inventory for a product at a specific store location
    @PutMapping("/inventory/{inventoryId}")
    @PreAuthorize("hasAnyRole('STORE_MANAGER', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductInventory> updateInventory(
            @PathVariable Long inventoryId,
            @RequestBody ProductInventory inventory) {
        inventory.setId(inventoryId);
        ProductInventory updatedInventory = storeService.updateInventory(inventory);
        return ResponseEntity.ok(updatedInventory);
    }

    // Get inventory for a specific product across all stores
    @GetMapping("/inventory/product/{productId}")
    public ResponseEntity<List<ProductInventory>> getInventoryByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(storeService.getInventoryByProduct(productId));
    }

    // Get all inventory for a specific store
    @GetMapping("/inventory/store/{storeId}")
    public ResponseEntity<List<ProductInventory>> getInventoryByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getInventoryByStore(storeId));
    }
}