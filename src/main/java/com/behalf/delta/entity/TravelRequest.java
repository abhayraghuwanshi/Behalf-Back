package com.behalf.delta.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "travel_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String fromLocation;
    private String toLocation;
    private String travelDate;
    private Double price;
}

