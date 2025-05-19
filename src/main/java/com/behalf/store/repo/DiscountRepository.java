package com.behalf.store.repo;

import com.behalf.store.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT d FROM Discount d " +
            "WHERE d.product.id = :productId " +
            "AND (d.store.id IS NULL OR d.store.id = :storeId) " +
            "AND d.isActive = true " +
            "AND :today BETWEEN d.startDate AND d.endDate")
    Discount findActiveDiscount(@Param("productId") Long productId,
                                @Param("storeId") Long storeId,
                                @Param("today") LocalDate today);

    List<Discount> findByStoreIdInAndProductIdAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            List<Long> storeIds, Long productId, LocalDate startDate, LocalDate endDate);

    Discount findFirstByProductIdAndStoreIsNullAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
            Long productId, LocalDate startDate, LocalDate endDate);

}
