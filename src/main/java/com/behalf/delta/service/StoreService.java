package com.behalf.delta.service;


import com.behalf.delta.repo.StoreRepository;
import com.behalf.delta.entity.StoreOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public List<StoreOrder> getAllOrders() {
        return storeRepository.findAll();
    }

    public List<StoreOrder> getOrdersByStatus(String status) {
        return storeRepository.findByStatus(status);
    }

    public StoreOrder placeOrder(StoreOrder storeOrder) {
        storeOrder.setStatus("PLACED");
        return storeRepository.save(storeOrder);
    }

    public StoreOrder checkout(StoreOrder storeOrder) {
        storeOrder.setStatus("CHECKEDOUT");
        return storeRepository.save(storeOrder);
    }

    public List<StoreOrder> getOrdersByUser(Long userId) {
        return storeRepository.findByUserId(userId);
    }
}
