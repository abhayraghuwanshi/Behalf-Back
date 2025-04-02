package com.behalf.delta.repo;

import com.behalf.delta.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // Add custom query methods if needed
}