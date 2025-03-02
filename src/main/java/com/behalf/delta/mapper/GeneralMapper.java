package com.behalf.delta.mapper;

import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.entity.dto.QuestMetadataDTO;
import org.springframework.web.bind.annotation.PostMapping;

public class GeneralMapper {


    public static QuestMetadataDTO convert(QuestMetadata questMetadata, UserInformation userInformation){
        return QuestMetadataDTO.builder()
                .questReward(questMetadata.getQuestReward())
                .creationTimestamp(questMetadata.getCreationTimestamp())
                .questStatus(questMetadata.getQuestStatus())
                .questInstructions(questMetadata.getQuestInstructions())
                .questValidity(questMetadata.getQuestValidity())
                .userInformation(userInformation)
                .build();
    }
}
