package com.behalf.delta.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_inventory")
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Store product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Integer quantity;

    // Additional fields that might be useful
    private String sku; // Stock keeping unit
    private String locationWithinStore; // e.g., "Aisle 5, Shelf 3"
    private Integer reorderLevel; // Minimum quantity before reordering
    private Integer reorderQuantity; // How many to order when restocking
}