package com.behalf.store.repo;

import com.behalf.store.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> findByUserIdOrSessionId(Long userId, String sessionId);
}
