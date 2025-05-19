package com.behalf.store.service.impl;

import com.behalf.store.mapper.ShopMapper;
import com.behalf.store.model.*;
import com.behalf.store.model.dto.CartDTO;
import com.behalf.store.repo.*;
import com.behalf.store.service.StoreCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreCartServiceImpl implements StoreCartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ProductPriceRepository productPriceRepository;
    private  final DiscountRepository discountRepository;

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
        CartDTO cartDTO =  ShopMapper.toDTO(findOrCreateCart(userId, sessionId));
        LocalDate date = LocalDate.now();
        cartDTO.getItems().forEach(cartItemDTO -> {
            ProductPrice price = productPriceRepository.findActivePrice(cartItemDTO.getProductId(), cartItemDTO.getStoreId(), date);
            Discount discount = discountRepository.findActiveDiscount(cartItemDTO.getProductId(), cartItemDTO.getStoreId(), date);
            BigDecimal originalPrice = BigDecimal.valueOf(price.getPrice());
            BigDecimal discountPrice = discount != null ? discount.getDiscountPrice() : originalPrice;

            BigDecimal discountAmount = originalPrice.subtract(discountPrice);
            Double discountPercent = discountAmount.divide(originalPrice, 2, RoundingMode.HALF_UP).doubleValue() * 100;
            cartItemDTO.setOriginalPrice(originalPrice);
            cartItemDTO.setDiscountPrice(discountPrice);
            cartItemDTO.setDiscountAmount(discountAmount);
            cartItemDTO.setDiscountPercent(discountPercent);
            cartItemDTO.setCurrencyCode(price.getCurrencyCode());
        });
        return cartDTO;
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
