package com.behalf.store.service;

import com.behalf.store.model.Discount;

import java.util.List;

public interface DiscountService {
    Discount createDiscount(Discount discount);
    Discount updateDiscount(Long id, Discount updatedDiscount);
    Discount getDiscountById(Long id);
    List<Discount> getAllDiscounts();
    void deleteDiscount(Long id);
}
