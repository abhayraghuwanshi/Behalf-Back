package com.behalf.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double pricePerUnit;
    private Double totalPrice;
}
