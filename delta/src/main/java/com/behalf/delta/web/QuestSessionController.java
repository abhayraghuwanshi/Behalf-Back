package com.behalf.delta.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.dto.QuestDto;
import com.behalf.delta.service.QuestService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.dto.ChatSessionDTO;
import com.behalf.delta.entity.dto.MessageDTO;
import com.behalf.delta.service.ChatService;


@RestController
@RequestMapping("/api/quest/session")
public class QuestSessionController {
    private final ChatService chatService;

    private final QuestService questService;

    public QuestSessionController(ChatService chatService, QuestService questService) {
        this.chatService = chatService;
        this.questService = questService;
    }

    @PostMapping() // create new chat session
    public ChatSessionDTO createChatSession(@RequestBody QuestSession sessionRequest,  @AuthenticationPrincipal OidcUser user) {
        QuestSession questSession = chatService.createChatSession(sessionRequest);
        return ChatSessionDTO.builder()
                .questStatus(questSession.getQuestStatus())
                .questId(questSession.getQuestId())
                .questCreatorId(questSession.getQuestCreatorId())
                .questAcceptor(questSession.getQuestAcceptor()).build();
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
    public QuestDto fetchChatSession(@PathVariable Long userID){
        Map<Long,List<ChatSessionDTO>> chats =  chatService.fetchChats(userID).stream().map(questSession ->
                ChatSessionDTO.builder()
                        .questStatus(questSession.getQuestStatus())
                        .questId(questSession.getQuestId())
                        .questCreatorId(questSession.getQuestCreatorId())
                        .id(questSession.getId())
                        .questAcceptor(questSession.getQuestAcceptor()).build()
        ).collect(Collectors.groupingBy(ChatSessionDTO::getQuestId, Collectors.toList()));

        List<QuestMetadata> metadataList = questService.fetchQuest(chats.keySet().stream().toList());

        return new QuestDto(metadataList, chats);

    }





}
