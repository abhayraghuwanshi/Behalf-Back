package com.behalf.store.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceDTO {
    private Long id;
    private Long productId;
    private Long storeId;
    private Double price;
    private Double discount;
    private String currencyCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
