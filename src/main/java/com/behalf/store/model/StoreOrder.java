package com.behalf.store.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")  // “order” is a reserved keyword in SQL.
@Setter
@Getter
public class StoreOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long userId;  // To keep track of the user who placed the order.
    private String address;
    private Double price;
    private String status;
    private String country;

}
