package com.behalf.delta.entity.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageDTO {
    private Long id;
    private String sender;
    private String recipient;
    private String timestamp;
    private String message;

    public MessageDTO(Long id, String sender, String recipient, String timestamp, String message) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.message = message;
    }

    // Getters and setters
}
