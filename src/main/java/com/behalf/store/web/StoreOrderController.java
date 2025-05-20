package com.behalf.store.web;


import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.service.AuthService;
import com.behalf.store.mapper.ShopMapper;
import com.behalf.store.model.StoreOrder;
import com.behalf.store.model.dto.StoreOrderDTO;
import com.behalf.store.service.StoreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store-orders")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService storeOrderService;

    private final AuthService authService;

    @PostMapping("/place-from-cart")
    public ResponseEntity<StoreOrderDTO> placeOrderFromCart(
            @RequestParam Long userId,
            @RequestParam String address,
            @RequestParam String country) {

        StoreOrder order = storeOrderService.placeOrderFromCart(userId, address, country);
        return ResponseEntity.ok(ShopMapper.toDTO(order));
    }

    @GetMapping
    public List<StoreOrderDTO> getUserOrders(@AuthenticationPrincipal OidcUser oidcUser) {
        UserInformation user = authService.getCurrentUser(oidcUser);
        List<StoreOrder> orders = storeOrderService.findByUserIdOrderByCreatedAtDesc(user.getId());
        return orders.stream().map(ShopMapper::toDTO).toList();
    }

}
