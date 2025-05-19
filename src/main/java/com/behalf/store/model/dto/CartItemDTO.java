package com.behalf.store.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

    // Enriched Pricing Fields
    private BigDecimal originalPrice;    // From ProductPrice
    private BigDecimal discountPrice;    // Final price after discount (can be from Discount entity or calculated)
    private BigDecimal discountAmount;   // originalPrice - discountPrice
    private Double discountPercent;      // Computed: (discountAmount / originalPrice) * 100
    private String currencyCode;         // From ProductPrice
}
