package com.behalf.delta.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSessionDTO {
    private Long id;
    private long questId;
    private long questAcceptor;

    public ChatSessionDTO(Long id, long questId, long questAcceptor) {
        this.id = id;
        this.questId = questId;
        this.questAcceptor = questAcceptor;
    }
}
