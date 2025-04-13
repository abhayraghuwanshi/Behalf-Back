package com.behalf.store.service.impl;

import com.behalf.store.mapper.CartMapper;
import com.behalf.store.model.Cart;
import com.behalf.store.model.CartItem;
import com.behalf.store.model.Product;
import com.behalf.store.model.Store;
import com.behalf.store.model.dto.CartDTO;
import com.behalf.store.repo.CartRepository;
import com.behalf.store.repo.ProductRepository;
import com.behalf.store.repo.StoreRepository;
import com.behalf.store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    private Cart findOrCreateCart(Long userId, String sessionId) {
        return cartRepository.findByUserIdOrSessionId(userId, sessionId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setSessionId(sessionId);
                    cart.setCreatedAt(LocalDateTime.now());
                    cart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    @Override
    public CartDTO getCart(Long userId, String sessionId) {
        return CartMapper.toDTO(findOrCreateCart(userId, sessionId));
    }

    @Override
    public void addItem(Long userId, String sessionId, Long productId, Long storeId, int quantity) {
        Cart cart = findOrCreateCart(userId, sessionId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("Store not found"));

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setStore(store);
            item.setQuantity(quantity);

            cart.getItems().add(item);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public void updateItem(Long userId, String sessionId, Long productId, int quantity) {
        Cart cart = findOrCreateCart(userId, sessionId);
        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public void removeItem(Long userId, String sessionId, Long productId) {
        Cart cart = findOrCreateCart(userId, sessionId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId, String sessionId) {
        Cart cart = findOrCreateCart(userId, sessionId);
        cart.getItems().clear();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }
}
