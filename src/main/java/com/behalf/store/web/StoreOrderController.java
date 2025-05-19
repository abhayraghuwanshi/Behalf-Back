package com.behalf.store.web;


import com.behalf.store.mapper.ShopMapper;
import com.behalf.store.model.StoreOrder;
import com.behalf.store.model.dto.StoreOrderDTO;
import com.behalf.store.service.StoreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService storeOrderService;

    @PostMapping("/place-from-cart")
    public ResponseEntity<StoreOrderDTO> placeOrderFromCart(
            @RequestParam Long userId,
            @RequestParam String address,
            @RequestParam String country) {

        StoreOrder order = storeOrderService.placeOrderFromCart(userId, address, country);
        return ResponseEntity.ok(ShopMapper.toDTO(order));
    }

    @GetMapping("/user/{userId}")
    public List<StoreOrderDTO> getUserOrders(@PathVariable Long userId) {
        List<StoreOrder> orders = storeOrderService.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(ShopMapper::toDTO)
                .toList();
    }

}
