package com.behalf.store.repo;

import com.behalf.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE :country IS NULL OR :country = '' OR s.country = :country")
    List<Store> findAllByCountry(@Param("country") String country);
}