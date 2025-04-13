package com.behalf.store.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.Setter;

@Entity
@Setter
public class StoreOrderItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private StoreOrder order;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private Double pricePerUnit;
    private Double totalPrice;
}
