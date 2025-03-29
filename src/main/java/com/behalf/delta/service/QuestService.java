package com.behalf.delta.service;


import com.behalf.delta.cache.MyCacheManager;
import com.behalf.delta.entity.Message;
import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.entity.dto.ChatSessionDTO;
import com.behalf.delta.entity.dto.QuestDto;
import com.behalf.delta.entity.dto.QuestMetadataDTO;
import com.behalf.delta.exception.DatabaseException;
import com.behalf.delta.exception.UserNotFoundException;
import com.behalf.delta.repo.QuestSessionRepository;
import com.behalf.delta.repo.MessageRepository;
import com.behalf.delta.repo.QuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestService {

    private final QuestSessionService questSessionService;

    private final QuestRepository questRepository;

    private final QuestSessionRepository questSessionRepository;

    private final MessageRepository messageRepository;

    private final MyCacheManager myCacheManager;



    public QuestMetadata createQuest(QuestMetadata quest) {
        try {
            var questCreated =  saveQuestToDatabase(quest);
            addQuestToCache(questCreated);
            return questCreated;
        } catch (DatabaseException e) {
            log.error("Database error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred");
        } catch (UserNotFoundException userNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    @CachePut(value = "questCache", key = "#quest.locationTo")
    public QuestMetadataDTO addQuestToCache(QuestMetadata quest) throws UserNotFoundException {
        log.info("Adding the cache");
        UserInformation userInformation =  myCacheManager.getUserInfoFromCache();

        return QuestMetadataDTO.builder()
                .questReward(quest.getQuestReward())
                .questValidity(quest.getQuestValidity())
                .questInstructions(quest.getQuestInstructions())
                .questCurrency(quest.getQuestCurrency())
                .creationTimestamp(quest.getCreationTimestamp())
                .questStatus(quest.getQuestStatus())
                .imageUrl(quest.getImageUrl())
                .locationFrom(quest.getLocationFrom())
                .locationTo(quest.getLocationTo())
                .questCreatorId(quest.getQuestCreatorId())
                .userInformation(userInformation)
                .build();

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



    public String updateQuest(Long questSessionId, QuestSession questSession){

            if ("SUCCESS".equals(questSession.getQuestStatus())){
                Optional<QuestMetadata> qm = questRepository.findById(questSession.getQuestId());
                if (qm.isPresent()){
                    qm.get().setQuestStatus(questSession.getQuestStatus());
                    questRepository.save(qm.get());
                }
                questSessionRepository.save(questSession);
                return "Success";
            } else {
            return "Failure";
        }
    }

    @Cacheable(value = "questCache", key = "#userCountry")
    public List<QuestMetadataDTO> fetchAllQuests(String userCountry) {
        return questRepository.fetchMergedQuestData(userCountry);
    }

    public QuestDto fetchById (Long userID){
            // Step 1: Fetch and map chat sessions for the user
            List<ChatSessionDTO> chatSessionDTOs = questSessionService.fetchChats(userID).stream()
                    .map(cs -> ChatSessionDTO.builder()
                            .id(cs.getId())
                            .questId(cs.getQuestId())
                            .questCreatorId(cs.getQuestCreatorId())
                            .questAcceptorId(cs.getQuestAcceptorId())
                            .questStatus(cs.getQuestStatus())
                            .build())
                    .toList();

            // Group chat sessions by questId
            Map<Long, List<ChatSessionDTO>> chats = chatSessionDTOs.stream()
                    .collect(Collectors.groupingBy(ChatSessionDTO::getQuestId));

            // Step 2: Determine the involved quest IDs from chat sessions
            Set<Long> involvedQuestIds = chats.keySet();

            // Step 3: Fetch quests using a single query that returns quests either:
            // - That the user is involved in (quest id in involvedQuestIds), OR
            // - That the user created (questCreatorId equals userID)
            List<QuestMetadata> quests = questRepository.fetchQuestsByUserInvolvement(userID, involvedQuestIds);

            // Step 4: Return the merged result as a QuestDto
            return new QuestDto(quests, chats);

    }

    public QuestMetadataDTO fetchASingleQuest(Integer questId){
        return questRepository.fetchAQuestData(questId);
    }



}
