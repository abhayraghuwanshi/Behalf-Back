package com.behalf.delta.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.Message;
import com.behalf.delta.repo.QuestSessionRepository;
import com.behalf.delta.repo.MessageRepository;

@Service
public class QuestSessionService {
    private final QuestSessionRepository questSessionRepository;
    private final MessageRepository messageRepository;

    public QuestSession getChatSession(Long id) {
        return questSessionRepository.getReferenceById(id);
    }

    public QuestSessionService(QuestSessionRepository questSessionRepository,
                               MessageRepository messageRepository) {
        this.questSessionRepository = questSessionRepository;
        this.messageRepository = messageRepository;
    }

    public QuestSession createChatSession(QuestSession questSession) {
        return questSessionRepository.save(questSession);
    }

    public Message addMessage(Long sessionId, String sender, String recipient, String messageText) {
        QuestSession session = questSessionRepository.findById(sessionId)
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
        return questSessionRepository.findAllByQuestAcceptorIdOrQuestCreatorId(userId, userId);
    }
}
