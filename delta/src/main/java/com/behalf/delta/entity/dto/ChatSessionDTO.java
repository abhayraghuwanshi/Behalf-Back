package com.behalf.delta.entity.dto;

import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.QuestMetadata;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChatSessionDTO {
    private Long id;

    private Long questId;

    private String questStatus;

    private Long questAcceptor;
    private Long questCreatorId;




}
