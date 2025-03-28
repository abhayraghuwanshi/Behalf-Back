package com.behalf.delta.web;

import com.behalf.delta.service.ProductService;
import com.behalf.delta.service.StoreService;
import com.behalf.delta.entity.StoreProduct;
import com.behalf.delta.entity.StoreOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/store")
public class QuestStoreController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    // Get products with optional filtering by name and price
    @GetMapping("/products")
    public List<StoreProduct> getProducts(@RequestParam(required = false) String name,
                                          @RequestParam(required = false) Double price) {
        return productService.getProducts(name, price);
    }

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
}

