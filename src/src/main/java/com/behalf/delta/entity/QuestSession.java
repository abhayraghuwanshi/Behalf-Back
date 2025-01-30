package com.behalf.delta.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class QuestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questCreatorId;

    @NotNull
    private Long questId;

    @Transient
    private String questRequestMsg;

    @NotNull
    private String questStatus;

    @NotNull
    private Long questAcceptorId;

    @OneToMany(mappedBy = "questSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> chats;

    // Getters and Setters
}
