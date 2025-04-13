package com.behalf.store.mapper;

import com.behalf.store.model.Cart;
import com.behalf.store.model.dto.CartDTO;
import com.behalf.store.model.dto.CartItemDTO;

import java.util.List;

public class CartMapper {

    public static CartDTO toDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setSessionId(cart.getSessionId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        List<CartItemDTO> itemDTOs = cart.getItems().stream().map(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setProductImage(
                    item.getProduct().getImageUrls() != null && !item.getProduct().getImageUrls().isEmpty()
                            ? item.getProduct().getImageUrls().get(0)
                            : null
            );
            itemDTO.setStoreId(item.getStore().getId());
            itemDTO.setStoreName(item.getStore().getName());
            itemDTO.setQuantity(item.getQuantity());
            return itemDTO;
        }).toList();

        dto.setItems(itemDTOs);
        return dto;
    }
}
