package com.behalf.store.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Optional: if the discount only applies in certain stores
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private BigDecimal discountPrice; // Final price after discount
    private LocalDate startDate;
    private LocalDate endDate;

    private String description;
    private Boolean isActive = true;
}
