package com.behalf.store.model.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String sessionId;
    private Long productId;
    private Long storeId;
    private Integer quantity;
}
