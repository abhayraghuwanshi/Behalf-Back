package com.behalf.store.repo;

import com.behalf.store.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStoreIdInAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            List<Long> storeIds, LocalDate start, LocalDate end
    );

    List<Discount> findByProductIdInAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            List<Long> productIds, LocalDate start, LocalDate end
    );

    Optional<Discount> findTopByProductIdAndStoreIdAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            Long productId, Long storeId, LocalDate from, LocalDate to);
}
