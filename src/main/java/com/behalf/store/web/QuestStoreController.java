package com.behalf.store.web;

import com.behalf.store.repo.InventoryService;
import com.behalf.store.service.StoreOrderService;
import com.behalf.store.service.StoreService;
import com.behalf.store.model.Store;
import com.behalf.store.model.StoreOrder;
import com.behalf.store.model.ProductInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestStoreController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private StoreOrderService storeOrderService;

    // Get all orders or filter by status (e.g., "OUT_OF_STOCK")
    @GetMapping("store/orders")
    public List<StoreOrder> getOrders(@RequestParam(required = false) String status) {
        if(status != null) {
            return storeOrderService.getOrdersByStatus(status);
        }
        return storeOrderService.getAllOrders();
    }

    // Place an order
    @PostMapping("store/orders")
    public StoreOrder placeOrder(@RequestBody StoreOrder storeOrder) {
        return storeOrderService.placeOrder(storeOrder);
    }

    // Checkout – add additional information such as address, price, etc.
    @PostMapping("store/checkout")
    public StoreOrder checkout(@RequestBody StoreOrder storeOrder) {
        return storeOrderService.checkout(storeOrder);
    }

    // Display orders for a specific user
    @GetMapping("store/myorders")
    public List<StoreOrder> getMyOrders(@RequestParam Long userId) {
        return storeOrderService.getOrdersByUser(userId);
    }


    // Create a new store - restricted to admin users
    @PostMapping("/stores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        Store createdStore = storeService.createStore(store);
        return ResponseEntity.ok(createdStore);
    }

    // Get all stores
    @GetMapping("/stores/fetch")
    public ResponseEntity<List<Store>> getAllStores(@RequestParam(required = false) String country) {
        return ResponseEntity.ok(storeService.getAllStores(country));
    }



    // Add inventory for a product at a specific store location
    @PostMapping("/inventory")
//    @PreAuthorize("hasRole('STORE_MANAGER')")
    public ResponseEntity<ProductInventory> addInventory(@RequestBody ProductInventory inventory) {
        ProductInventory addedInventory = inventoryService.addInventory(inventory);
        return ResponseEntity.ok(addedInventory);
    }

    // Update inventory for a product at a specific store location
    @PutMapping("/inventory/{inventoryId}")
//    @PreAuthorize("hasAnyRole('STORE_MANAGER', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductInventory> updateInventory(
            @PathVariable Long inventoryId,
            @RequestBody ProductInventory inventory) {
        inventory.setId(inventoryId);
        ProductInventory updatedInventory = inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(updatedInventory);
    }

    // Get inventory for a specific product across all stores
    @GetMapping("/inventory")
    public ResponseEntity<List<ProductInventory>> getInventoryByProduct() {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct());
    }

    // Get all inventory for a specific store
    @GetMapping("/inventory/store/{storeId}")
    public ResponseEntity<List<ProductInventory>> getInventoryByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(inventoryService.getInventoryByStore(storeId));
    }
}