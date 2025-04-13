package com.behalf.store.service;

import com.behalf.store.model.StoreOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface IStoreOrderService {
    StoreOrder placeOrderFromCart(Long userId, String address, String country);
}
