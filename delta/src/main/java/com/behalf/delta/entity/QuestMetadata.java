package com.behalf.delta.entity;

import java.time.Instant;
import java.util.Date;
import com.behalf.delta.constants.City;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    private String questInstructions;

    private String questValidity;

    private Integer questReward;

    private Date creationTimestamp;

    private Date lastModifiedTimestamp;

    private String acceptor;

    private Long paymentId;

    @PrePersist
    void onCreate() {
        this.creationTimestamp = Date.from(Instant.now());
    }

    // This method will set the last modified timestamp every time the entity is updated
    @PreUpdate
    void onUpdate() {
        this.lastModifiedTimestamp = Date.from(Instant.now());
    }

}
