package com.behalf.delta.service;

import com.behalf.delta.entity.StoreOrder;

import java.util.List;

public interface StoreOrderService {
    List<StoreOrder> getOrdersByStatus(String status);
    List<StoreOrder> getAllOrders();
    StoreOrder placeOrder(StoreOrder storeOrder);
    StoreOrder checkout(StoreOrder storeOrder);
    List<StoreOrder> getOrdersByUser(Long userId);
}
