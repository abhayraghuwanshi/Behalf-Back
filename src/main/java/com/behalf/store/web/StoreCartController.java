package com.behalf.store.web;

import com.behalf.store.model.Cart;
import com.behalf.store.model.dto.CartDTO;
import com.behalf.store.model.dto.CartItemRequest;
import com.behalf.store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class StoreCartController {

    private final CartService cartService;

    private Long getUserId() {
        return null; // replace with SecurityContextHolder if needed
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestParam(required = false) String sessionId) {
        return ResponseEntity.ok(cartService.getCart(getUserId(), sessionId));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItem(@RequestBody CartItemRequest request) {
        cartService.addItem(getUserId(), request.getSessionId(), request.getProductId(), request.getStoreId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateItem(@RequestBody CartItemRequest request) {
        cartService.updateItem(getUserId(), request.getSessionId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId, @RequestParam String sessionId) {
        cartService.removeItem(getUserId(), sessionId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam String sessionId) {
        cartService.clearCart(getUserId(), sessionId);
        return ResponseEntity.ok().build();
    }
}
