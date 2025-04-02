package com.behalf.delta.repo;

import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.dto.QuestMetadataDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestRepository extends JpaRepository<QuestMetadata, Long> {

    List<QuestMetadata> findAllByQuestCreatorId(Long id);

    @Query("SELECT new com.behalf.delta.entity.dto.QuestMetadataDTO(" +
            "q.id, q.questCreatorId, q.questInstructions, q.questValidity, " +
            "q.questReward, q.creationTimestamp, q.lastModifiedTimestamp, " +
            "q.questStatus, q.imageUrl, q.locationFrom, q.locationTo, q.questCurrency, u) " +
            "FROM QuestMetadata q " +
            "JOIN UserInformation u ON q.questCreatorId = u.id " +
            "WHERE ( q.locationFrom = :location OR q.locationTo = :location)")
    List<QuestMetadataDTO> fetchMergedQuestData(@Param("location") String location);

    @Query("SELECT q FROM QuestMetadata q WHERE q.questCreatorId = :userID OR q.id IN :involvedQuestIds")
    List<QuestMetadata> fetchQuestsByUserInvolvement(@Param("userID") Long userID,
                                                     @Param("involvedQuestIds") Set<Long> involvedQuestIds);

    @Query("SELECT q FROM QuestMetadata q WHERE q.id != :questId " +
            "AND (:questRewardMin IS NULL OR q.questReward >= :questRewardMin) " +
            "AND (:questRewardMax IS NULL OR q.questReward <= :questRewardMax) " +
            "AND (:status IS NULL OR q.questStatus = :status) " +
            "ORDER BY q.creationTimestamp DESC")
    List<QuestMetadata> findSimilarQuests(
            @Param("questId") Long questId,
            @Param("questRewardMin") Integer questRewardMin,
            @Param("questRewardMax") Integer questRewardMax,
            @Param("status") String status,
            Pageable pageable);

    /**
     * Find similar quests excluding those from the same creator
     */
    @Query("SELECT q FROM QuestMetadata q WHERE q.id != :questId " +
            "AND q.questCreatorId != :creatorId " +
            "AND (:questRewardMin IS NULL OR q.questReward >= :questRewardMin) " +
            "AND (:questRewardMax IS NULL OR q.questReward <= :questRewardMax) " +
            "AND (:status IS NULL OR q.questStatus = :status) " +
            "ORDER BY q.creationTimestamp DESC")
    List<QuestMetadata> findSimilarQuestsExcludingCreator(
            @Param("questId") Long questId,
            @Param("creatorId") Long creatorId,
            @Param("questRewardMin") Integer questRewardMin,
            @Param("questRewardMax") Integer questRewardMax,
            @Param("status") String status,
            Pageable pageable);


    @Query("SELECT new com.behalf.delta.entity.dto.QuestMetadataDTO( " +
            "q.id, q.questCreatorId, q.questInstructions, q.questValidity, " +
            "q.questReward, q.creationTimestamp, q.lastModifiedTimestamp, " +
            "q.questStatus, q.imageUrl,q.locationFrom, q.locationTo, q.questCurrency, u) " +
            "FROM QuestMetadata q " +
            "JOIN UserInformation u ON q.questCreatorId = u.id " +
            "WHERE q.id = :questId")
    QuestMetadataDTO fetchAQuestData(@Param("questId") Integer questId);



}
