package com.behalf.store.repo;

import com.behalf.store.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    // Add filtering logic if needed, e.g., by store or product
}
