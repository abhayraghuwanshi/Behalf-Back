package com.behalf.store.service;

import com.behalf.store.model.Cart;
import com.behalf.store.model.dto.CartDTO;

public interface CartService {
    CartDTO getCart(Long userId, String sessionId);
    void addItem(Long userId, String sessionId, Long productId, Long storeId, int quantity);
    void updateItem(Long userId, String sessionId, Long productId, int quantity);
    void removeItem(Long userId, String sessionId, Long productId);
    void clearCart(Long userId, String sessionId);
}
