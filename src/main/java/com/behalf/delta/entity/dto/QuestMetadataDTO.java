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
@AllArgsConstructor
public class QuestMetadataDTO implements Serializable {
    private Long id;
    private Long questCreatorId;
    private String questInstructions;
    private Date questValidity;
    private Integer questReward;
    private Date creationTimestamp;
    private Date lastModifiedTimestamp;
    private String questStatus;
    private String imageUrl;
    private String locationFrom;
    private String locationTo;
    private String questCurrency;
    private UserInformation userInformation;
}
