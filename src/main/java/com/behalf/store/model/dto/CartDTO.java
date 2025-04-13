package com.behalf.store.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private Long userId;
    private String sessionId;
    private List<CartItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

