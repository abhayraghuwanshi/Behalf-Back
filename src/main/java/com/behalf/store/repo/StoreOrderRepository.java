package com.behalf.store.repo;

import com.behalf.store.model.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {

    List<StoreOrder> findByStatus(String status);

    List<StoreOrder> findByUserId(Long userId);
}
