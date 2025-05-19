package com.behalf.delta.web;

import java.util.*;
import java.util.stream.Collectors;

import com.behalf.delta.entity.dto.QuestDto;
import com.behalf.delta.service.QuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.dto.ChatSessionDTO;
import com.behalf.delta.entity.dto.MessageDTO;
import com.behalf.delta.service.QuestSessionService;


@RestController
@RequestMapping("/api/quests/sessions")
@Slf4j
public class QuestSessionController {
    private final QuestSessionService questSessionService;

    private final QuestService questService;

    public QuestSessionController(QuestSessionService questSessionService, QuestService questService) {
        this.questSessionService = questSessionService;
        this.questService = questService;
    }

    @PostMapping() // create new chat session
    @PreAuthorize("@securityService.isSameUser(authentication.principal.email, #sessionRequest.questAcceptorId)")
    public ChatSessionDTO createChatSession(@RequestBody QuestSession sessionRequest) {
        QuestSession questSession = questSessionService.createChatSession(sessionRequest);
        return ChatSessionDTO.builder()
                .questStatus(questSession.getQuestStatus())
                .questId(questSession.getQuestId())
                .questCreatorId(questSession.getQuestCreatorId())
                .questAcceptorId(questSession.getQuestAcceptorId()).build();
    }

    @PostMapping("/{sessionId}/messages") // post message per session
//    @PreAuthorize("@securityService.isSameUser(authentication.principal.email, #messageRequest.questAcceptorId)")
    public MessageDTO addMessage(@PathVariable Long sessionId,
            @RequestBody Message messageRequest) {
        Message message = questSessionService.addMessage(sessionId, messageRequest.getSender(),
                messageRequest.getRecipient(), messageRequest.getMessage());
        return new MessageDTO(message.getId(), message.getSender(), message.getRecipient(),
                message.getTimestamp().toString(), message.getMessage());
    }

    @GetMapping("/{sessionId}/messages") // get all messages per session
    public List<MessageDTO> getMessages(@PathVariable Long sessionId) {
        QuestSession session = questSessionService.getChatSession(sessionId);
        return session.getChats().stream()
                .map(message -> new MessageDTO(message.getId(), message.getSender(),
                        message.getRecipient(), message.getTimestamp().toString(),
                        message.getMessage()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userID}")
    public QuestDto fetchChatSession(@PathVariable Long userID) {
        return questService.fetchById(userID);
    }








}
