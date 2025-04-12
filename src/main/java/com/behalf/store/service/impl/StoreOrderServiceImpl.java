package com.behalf.store.service.impl;

import com.behalf.store.model.StoreOrder;
import com.behalf.store.repo.StoreOrderRepository;
import com.behalf.store.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreOrderServiceImpl implements StoreOrderService {

    @Autowired
    private StoreOrderRepository orderRepository;

    @Override
    public List<StoreOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<StoreOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public StoreOrder placeOrder(StoreOrder storeOrder) {
        return orderRepository.save(storeOrder);
    }

    @Override
    public StoreOrder checkout(StoreOrder storeOrder) {
        return orderRepository.save(storeOrder);
    }

    @Override
    public List<StoreOrder> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
