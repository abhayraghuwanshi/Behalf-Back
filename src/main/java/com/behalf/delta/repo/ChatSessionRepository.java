package com.behalf.delta.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.behalf.delta.entity.QuestSession;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<QuestSession, Long> {

    List<QuestSession> findAllByQuestAcceptorIdOrQuestCreatorId(Long id, Long creatorId);

}
