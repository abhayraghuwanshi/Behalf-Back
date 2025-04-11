package com.behalf.store.service;

import com.behalf.store.model.StoreOrder;

import java.util.List;

public interface StoreOrderService {
    List<StoreOrder> getOrdersByStatus(String status);
    List<StoreOrder> getAllOrders();
    StoreOrder placeOrder(StoreOrder storeOrder);
    StoreOrder checkout(StoreOrder storeOrder);
    List<StoreOrder> getOrdersByUser(Long userId);
}
