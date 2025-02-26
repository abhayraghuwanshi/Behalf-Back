package com.behalf.delta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    @NotNull
    private Long creatorId;
    @NotNull
    private String fromLocation;
    @NotNull
    private String toLocation;
    @NotNull
    private String travelDate;

    @OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

