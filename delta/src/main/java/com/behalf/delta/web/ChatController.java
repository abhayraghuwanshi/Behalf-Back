package com.behalf.delta.web;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.behalf.delta.entity.ChatSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.dto.ChatSessionDTO;
import com.behalf.delta.entity.dto.MessageDTO;
import com.behalf.delta.service.ChatService;



@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sessions")
    public ChatSessionDTO createChatSession(@RequestBody ChatSession sessionRequest) {
        ChatSession session = chatService.createChatSession(sessionRequest.getQuestId(),
                sessionRequest.getAuthor());
        return new ChatSessionDTO(session.getId(), session.getQuestId(), session.getAuthor());
    }

    @PostMapping("/{sessionId}/messages")
    public MessageDTO addMessage(@PathVariable Long sessionId,
            @RequestBody Message messageRequest) {
        Message message = chatService.addMessage(sessionId, messageRequest.getSender(),
                messageRequest.getRecipient(), messageRequest.getMessage());
        return new MessageDTO(message.getId(), message.getSender(), message.getRecipient(),
                message.getTimestamp().toString(), message.getMessage());
    }

    @GetMapping("/{sessionId}/messages")
    public List<MessageDTO> getMessages(@PathVariable Long sessionId) {
        ChatSession session = chatService.getChatSession(sessionId);
        return session.getChats().stream()
                .map(message -> new MessageDTO(message.getId(), message.getSender(),
                        message.getRecipient(), message.getTimestamp().toString(),
                        message.getMessage()))
                .collect(Collectors.toList());
    }
}
