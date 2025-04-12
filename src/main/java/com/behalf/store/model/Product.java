package com.behalf.store.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String sku;
    private String brand;

    private String category;
    private String unitOfMeasure;

    private Boolean isUniquePerStore = false;
    private Boolean isActive = true;
}
