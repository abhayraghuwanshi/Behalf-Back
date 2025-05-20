package com.behalf.store.web;

import com.behalf.delta.service.AuthService;
import com.behalf.store.model.dto.CartDTO;
import com.behalf.store.model.dto.CartItemRequest;
import com.behalf.store.service.StoreCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/api/store-cart")
@RequiredArgsConstructor
public class StoreCartController {

    private final StoreCartService storeCartService;

    private final AuthService authService;

    private Long getUserId() {
        return null; // replace with SecurityContextHolder if needed
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestParam(required = false) String sessionId) {
        return ResponseEntity.ok(storeCartService.getCart(getUserId(), sessionId));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItem(@RequestBody CartItemRequest request) {
        storeCartService.addItem(getUserId(), request.getSessionId(), request.getProductId(), request.getStoreId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateItem(@RequestBody CartItemRequest request) {
        storeCartService.updateItem(getUserId(), request.getSessionId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId, @RequestParam String sessionId) {
        storeCartService.removeItem(getUserId(), sessionId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam String sessionId) {
        storeCartService.clearCart(getUserId(), sessionId);
        return ResponseEntity.ok().build();
    }
}
