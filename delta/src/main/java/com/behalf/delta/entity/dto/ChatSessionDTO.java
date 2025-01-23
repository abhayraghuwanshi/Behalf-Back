package com.behalf.delta.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSessionDTO {
    private Long id;
    private String questId;
    private String author;

    public ChatSessionDTO(Long id, String questId, String author) {
        this.id = id;
        this.questId = questId;
        this.author = author;
    }
}
