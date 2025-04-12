package com.behalf.store.repo;

import com.behalf.store.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStoreIdInAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            List<Long> storeIds, LocalDate start, LocalDate end
    );

    List<Discount> findByProductIdInAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            List<Long> productIds, LocalDate start, LocalDate end
    );
}
