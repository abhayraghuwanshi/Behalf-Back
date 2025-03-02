package com.behalf.delta.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@CrossOrigin(origins = "*") // Allow CORS from your React frontend
public class QuestStoreController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // Get products with optional filtering by name and price
    @GetMapping("/products")
    public List<Product> getProducts(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Double price) {
        return productService.getProducts(name, price);
    }

    // Get all orders or filter by status (e.g., "OUT_OF_STOCK")
    @GetMapping("/orders")
    public List<Order> getOrders(@RequestParam(required = false) String status) {
        if(status != null) {
            return orderService.getOrdersByStatus(status);
        }
        return orderService.getAllOrders();
    }

    // Place an order
    @PostMapping("/orders")
    public Order placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    // Checkout – add additional information such as address, price, etc.
    @PostMapping("/checkout")
    public Order checkout(@RequestBody Order order) {
        return orderService.checkout(order);
    }

    // Display orders for a specific user
    @GetMapping("/myorders")
    public List<Order> getMyOrders(@RequestParam Long userId) {
        return orderService.getOrdersByUser(userId);
    }
}

