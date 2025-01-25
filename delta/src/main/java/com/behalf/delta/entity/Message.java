package com.behalf.delta.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient;
    private LocalDateTime timestamp;
    private String message;

    @ManyToOne
    @JoinColumn(name = "chat_session_id", nullable = false)
    private QuestSession questSession;

    // Getters and Setters
}

