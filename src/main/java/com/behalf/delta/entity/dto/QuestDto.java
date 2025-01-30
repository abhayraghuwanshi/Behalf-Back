package com.behalf.delta.entity.dto;

import com.behalf.delta.entity.QuestMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class QuestDto {

    private List<QuestMetadata> questMetadataList;

    private Map<Long, List<ChatSessionDTO>> chatSessionDTOList;
}
