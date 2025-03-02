package com.behalf.delta.entity.dto;


import com.behalf.delta.entity.UserInformation;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
public class QuestMetadataDTO implements Serializable {
    private Long id;
    private Long questCreatorId;

    private String questInstructions;
    private Date questValidity;

    private Integer questReward;
    private Date creationTimestamp;
    private Date lastModifiedTimestamp;
    private String questStatus;

    private UserInformation userInformation;

    public QuestMetadataDTO(Long id, Long questCreatorId, String questInstructions, Date questValidity,
                            Integer questReward, Date creationTimestamp, Date lastModifiedTimestamp,
                            String questStatus, UserInformation userInformation) {
        this.id = id;
        this.questCreatorId = questCreatorId;
        this.questInstructions = questInstructions;
        this.questValidity = questValidity;
        this.questReward = questReward;
        this.creationTimestamp = creationTimestamp;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.questStatus = questStatus;
        this.userInformation = userInformation;
    }

}
