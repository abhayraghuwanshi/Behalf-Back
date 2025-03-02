package com.behalf.delta.repo;

import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.entity.dto.QuestMetadataDTO;
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
            "q.questStatus, u) " +
            "FROM QuestMetadata q JOIN UserInformation u ON q.questCreatorId = u.id")
    List<QuestMetadataDTO> fetchMergedQuestData();

    @Query("SELECT q FROM QuestMetadata q WHERE q.questCreatorId = :userID OR q.id IN :involvedQuestIds")
    List<QuestMetadata> fetchQuestsByUserInvolvement(@Param("userID") Long userID,
                                                     @Param("involvedQuestIds") Set<Long> involvedQuestIds);

}
