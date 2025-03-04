package com.behalf.delta.service;


import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.exception.DatabaseException;
import com.behalf.delta.exception.WorkflowException;
import com.behalf.delta.repo.QuestSessionRepository;
import com.behalf.delta.repo.MessageRepository;
import com.behalf.delta.repo.QuestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class QuestService {


    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestSessionRepository questSessionRepository;

    @Autowired
    private MessageRepository messageRepository;


    public QuestMetadata placeOrder(QuestMetadata quest) {
        try {
            // Step 1: Persist the Quest to the repository
            QuestMetadata savedQuest = saveQuestToDatabase(quest);
            return savedQuest;
        } catch (DatabaseException e) {
            log.error("Database error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred");
        } catch (WorkflowException e) {
            log.error("Workflow error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Workflow service error occurred");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }


    // Helper method to save the Quest to the database
    private QuestMetadata saveQuestToDatabase(QuestMetadata quest) throws Exception {
        try {
            log.info("Saving quest to the database");
            return questRepository.saveAndFlush(quest);
        } catch (Exception e) {
            log.error("Failed to persist the quest in the repository", e);
            throw new DatabaseException("Database operation failed", e);
        }
    }

    // Helper method to start the Temporal workflow


    public void assignQuest(QuestSession questSession){
        try {
             QuestSession qs = questSessionRepository.saveAndFlush(questSession);
            messageRepository.save(Message.builder()
                    .sender(questSession.getQuestAcceptorId().toString())
                    .message(questSession.getQuestRequestMsg())
                    .recipient(questSession.getQuestAcceptorId().toString())
                            .questSession(qs)
                    .timestamp(LocalDateTime.now()).build());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Exception has occur");
        }

    }

    public List<QuestMetadata> fetchQuest(List<Long> longList){
        return questRepository.findAllById(longList);
    }

    public List<QuestMetadata> fetchQuestByCreatorId(Long id){
        return questRepository.findAllByQuestCreatorId(id);
    }



}
