package com.behalf.delta.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.repo.ChatSessionRepository;
import com.behalf.delta.repo.MessageRepository;

@Service
public class ChatService {
    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;

    public QuestSession getChatSession(Long id) {
        return chatSessionRepository.getReferenceById(id);
    }

    public ChatService(ChatSessionRepository chatSessionRepository,
            MessageRepository messageRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageRepository = messageRepository;
    }

    public QuestSession createChatSession(QuestSession questSession) {
        return chatSessionRepository.save(questSession);
    }

    public Message addMessage(Long sessionId, String sender, String recipient, String messageText) {
        QuestSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setTimestamp(LocalDateTime.now());
        message.setMessage(messageText);
        message.setQuestSession(session);

        return messageRepository.save(message);
    }

    public List<QuestSession> fetchChats(Long userId){
        return chatSessionRepository.findAllByQuestAcceptorIdOrQuestCreatorId(userId, userId);
    }
}
