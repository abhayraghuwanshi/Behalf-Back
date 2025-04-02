package com.behalf.delta.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestMetadata implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long questCreatorId;

    @NotEmpty
    private String questInstructions;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date questValidity;

    private Integer questReward;

    private Date creationTimestamp;

    private Date lastModifiedTimestamp;

    private String questStatus;

    private String locationFrom;

    @NotNull
    private String locationTo;

    private String locationToDetail;

    private String locationFromDetail;

    private String questCurrency;

    private String imageUrl;


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
