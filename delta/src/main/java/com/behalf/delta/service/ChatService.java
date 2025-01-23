package com.behalf.delta.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.behalf.delta.entity.ChatSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.repo.ChatSessionRepository;
import com.behalf.delta.repo.MessageRepository;

@Service
public class ChatService {
    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;

    public ChatSession getChatSession(Long id) {
        return chatSessionRepository.getReferenceById(id);
    }

    public ChatService(ChatSessionRepository chatSessionRepository,
            MessageRepository messageRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageRepository = messageRepository;
    }

    public ChatSession createChatSession(String questId, String author) {
        ChatSession session = new ChatSession();
        session.setQuestId(questId);
        session.setAuthor(author);
        return chatSessionRepository.save(session);
    }

    public Message addMessage(Long sessionId, String sender, String recipient, String messageText) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setTimestamp(LocalDateTime.now());
        message.setMessage(messageText);
        message.setChatSession(session);

        return messageRepository.save(message);
    }
}
