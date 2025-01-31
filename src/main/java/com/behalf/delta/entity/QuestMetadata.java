package com.behalf.delta.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import com.behalf.delta.constants.QuestCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import static jakarta.persistence.EnumType.STRING;

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

    @NotEmpty
    private String questValidity;

    @Enumerated(STRING)
    private QuestCategory questLabel;

    private Integer questReward;

    private Date creationTimestamp;

    private Date lastModifiedTimestamp;

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
