package com.behalf.store.web;


import com.behalf.store.model.StoreOrder;
import com.behalf.store.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class StoreOrderController {

    @Autowired
    private StoreOrderService storeOrderService;

    @GetMapping
    public ResponseEntity<List<StoreOrder>> getOrders(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(storeOrderService.getOrdersByStatus(status));
        }
        return ResponseEntity.ok(storeOrderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<StoreOrder> placeOrder(@RequestBody StoreOrder storeOrder) {
        return ResponseEntity.ok(storeOrderService.placeOrder(storeOrder));
    }

    @PostMapping("/checkout")
    public ResponseEntity<StoreOrder> checkout(@RequestBody StoreOrder storeOrder) {
        return ResponseEntity.ok(storeOrderService.checkout(storeOrder));
    }

    @GetMapping("/my")
    public ResponseEntity<List<StoreOrder>> getMyOrders(@RequestParam Long userId) {
        return ResponseEntity.ok(storeOrderService.getOrdersByUser(userId));
    }
}
