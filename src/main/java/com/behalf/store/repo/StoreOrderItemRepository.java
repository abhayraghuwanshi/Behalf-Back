package com.behalf.store.repo;

import com.behalf.store.model.StoreOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOrderItemRepository extends JpaRepository<StoreOrderItem, Long> {
}
