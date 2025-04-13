package com.behalf.store.repo;

import com.behalf.store.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByStoreIdInAndEffectiveFromBeforeAndEffectiveToAfter(
            List<Long> storeIds, LocalDate today1, LocalDate today2);

    Optional<ProductPrice> findTopByProductIdAndStoreIdAndEffectiveFromBeforeAndEffectiveToAfterOrderByEffectiveFromDesc(
            Long productId, Long storeId, LocalDate from, LocalDate to);
}
