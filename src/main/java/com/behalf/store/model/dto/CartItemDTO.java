package com.behalf.store.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Long storeId;
    private String storeName;
    private Integer quantity;
}
