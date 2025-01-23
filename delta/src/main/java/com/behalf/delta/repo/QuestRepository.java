package com.behalf.delta.repo;

import com.behalf.delta.entity.QuestMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<QuestMetadata, Long> {

}
