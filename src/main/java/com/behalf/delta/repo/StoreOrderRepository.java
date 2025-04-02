package com.behalf.delta.repo;

import com.behalf.delta.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
}
