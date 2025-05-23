package com.behalf.store.service;

import com.behalf.store.model.StoreOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOrderService {
    StoreOrder placeOrderFromCart(Long userId, String address, String country);

    List<StoreOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
}
