package com.behalf.delta.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private String email;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<ProductInventory> inventories;
}