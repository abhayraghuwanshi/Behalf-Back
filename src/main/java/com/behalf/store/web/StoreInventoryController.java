package com.behalf.store.web;

import com.behalf.store.model.ProductInventory;
import com.behalf.store.service.StoreInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class StoreInventoryController {

    @Autowired
    private StoreInventoryService inventoryService;

    @PostMapping
    // @PreAuthorize("hasRole('STORE_MANAGER')")
    public ResponseEntity<ProductInventory> addInventory(@RequestBody ProductInventory inventory) {
        return ResponseEntity.ok(inventoryService.addInventory(inventory));
    }

    @PutMapping("/{inventoryId}")
    // @PreAuthorize("hasAnyRole('STORE_MANAGER', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductInventory> updateInventory(@PathVariable Long inventoryId,
                                                            @RequestBody ProductInventory inventory) {
        inventory.setId(inventoryId);
        return ResponseEntity.ok(inventoryService.updateInventory(inventory));
    }

    @GetMapping("/by-product")
    public ResponseEntity<List<ProductInventory>> getInventoryByProduct() {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductInventory>> getInventoryByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(inventoryService.getInventoryByStore(storeId));
    }
}
