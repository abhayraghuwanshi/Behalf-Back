package com.behalf.reviews.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ratings")
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questId;      // NEW: reference quest

    private Long userId;       // user being rated

    private String userType;   // "creator" or "traveler"

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingDetail> ratingDetails;

}

