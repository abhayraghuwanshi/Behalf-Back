package com.behalf.delta.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.behalf.delta.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
