package com.behalf.delta.web;

import java.util.*;
import java.util.stream.Collectors;

import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.dto.QuestDto;
import com.behalf.delta.service.QuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.dto.ChatSessionDTO;
import com.behalf.delta.entity.dto.MessageDTO;
import com.behalf.delta.service.ChatService;


@RestController
@RequestMapping("/api/quests/sessions")
@Slf4j
public class QuestSessionController {
    private final ChatService chatService;

    private final QuestService questService;

    public QuestSessionController(ChatService chatService, QuestService questService) {
        this.chatService = chatService;
        this.questService = questService;
    }

    @PostMapping() // create new chat session
    public ChatSessionDTO createQuestSession(@RequestBody QuestSession sessionRequest) {
        QuestSession questSession = chatService.createChatSession(sessionRequest);
        return ChatSessionDTO.builder()
                .questStatus(questSession.getQuestStatus())
                .questId(questSession.getQuestId())
                .questCreatorId(questSession.getQuestCreatorId())
                .referId(questSession.getReferId())
                .questAcceptorId(questSession.getQuestAcceptorId()).build();
    }

    @PostMapping("/{sessionId}/messages") // post message per session
    public MessageDTO addMessage(@PathVariable Long sessionId,
            @RequestBody Message messageRequest) {
        Message message = chatService.addMessage(sessionId, messageRequest.getSender(),
                messageRequest.getRecipient(), messageRequest.getMessage());
        return new MessageDTO(message.getId(), message.getSender(), message.getRecipient(),
                message.getTimestamp().toString(), message.getMessage());
    }

    @GetMapping("/{sessionId}/messages") // get all messages per session
    public List<MessageDTO> getMessages(@PathVariable Long sessionId) {
        QuestSession session = chatService.getChatSession(sessionId);
        return session.getChats().stream()
                .map(message -> new MessageDTO(message.getId(), message.getSender(),
                        message.getRecipient(), message.getTimestamp().toString(),
                        message.getMessage()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userID}")
    public QuestDto fetchChatSession(@PathVariable Long userID) {
        // Add more auth

        // Step 1: Fetch chat sessions for the user
        Map<Long, List<ChatSessionDTO>> chats = chatService.fetchChats(userID).stream()
                .map(questSession -> ChatSessionDTO.builder()
                        .questStatus(questSession.getQuestStatus())
                        .questId(questSession.getQuestId())
                        .questCreatorId(questSession.getQuestCreatorId())
                        .id(questSession.getId())
                        .questAcceptorId(questSession.getQuestAcceptorId())
                        .referId(questSession.getReferId())
                        .build())
                .collect(Collectors.groupingBy(ChatSessionDTO::getQuestId, Collectors.toList()));

        // Step 2: Fetch quests where the user has active chat sessions
        List<QuestMetadata> metadataList = new ArrayList<>();
        metadataList.addAll(questService.fetchQuest(new ArrayList<>(chats.keySet())));

        log.info("metadataList" + metadataList.toString());


        // Step 3: Fetch all quests created by the user (handling possible null)
        List<QuestMetadata> createdQuests = Optional.ofNullable(questService.fetchQuestByCreatorId(userID))
                .orElse(Collections.emptyList()) // Ensure it's never null
                .stream()
                .filter(q -> !chats.containsKey(q.getId())) // Exclude already fetched quests
                .toList();

        // Step 4: Merge all fetched quests
        metadataList.addAll(createdQuests);

        return new QuestDto(metadataList, chats);
    }






}
