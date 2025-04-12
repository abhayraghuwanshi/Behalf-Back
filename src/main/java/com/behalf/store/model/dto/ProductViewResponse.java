package com.behalf.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewResponse {
    private Long productId;
    private String name;
    private String description;
    private String brand;
    private List<String> imageUrls;

    private String currency;
    private Double originalPrice;
    private Double discount;      // in percent (e.g., 10.0 for 10%)
    private Double finalPrice;

    private boolean outOfStock;
    private Integer quantityAvailable;

    private String storeName;
    private String storeLocation;
}
