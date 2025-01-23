package com.behalf.delta.repo;

import com.behalf.delta.entity.QuestAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestAgreementRepo extends JpaRepository<QuestAgreement, Long> {
}
