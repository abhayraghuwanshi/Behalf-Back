package com.behalf.store.service.impl;

import com.behalf.store.model.Discount;
import com.behalf.store.repo.DiscountRepository;
import com.behalf.store.service.StoreDiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreDiscountServiceImpl implements StoreDiscountService {

    private final DiscountRepository discountRepository;

    public StoreDiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Discount updateDiscount(Long id, Discount updatedDiscount) {
        Optional<Discount> existing = discountRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Discount not found with ID: " + id);
        }

        updatedDiscount.setId(id);
        return discountRepository.save(updatedDiscount);
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with ID: " + id));
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}
