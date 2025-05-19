package com.behalf.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderDTO {
    private Long orderId;
    private Long userId;
    private String address;
    private String country;
    private Double price;
    private Double discountPrice;
    private String status;
    private LocalDateTime createdAt;
    private List<StoreOrderItemDTO> items;
}
