package com.behalf.store.model;

import com.behalf.store.model.Product;
import com.behalf.store.model.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "product_price")
@Getter
@Setter
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Double price;

    private Double discount; // Optional - % or absolute

    private String currencyCode = "INR"; // or "USD", "EUR"

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
