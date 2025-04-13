package com.behalf.store.web;


import com.behalf.store.model.StoreOrder;
import com.behalf.store.service.IStoreOrderService;
import com.behalf.store.service.StoreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class StoreOrderController {

    private final IStoreOrderService storeOrderService;

    @PostMapping("/place-from-cart")
    public ResponseEntity<StoreOrder> placeOrderFromCart(
            @RequestParam Long userId,
            @RequestParam String address,
            @RequestParam String country
    ) {
        StoreOrder order = storeOrderService.placeOrderFromCart(userId, address, country);
        return ResponseEntity.ok(order);
    }
}
