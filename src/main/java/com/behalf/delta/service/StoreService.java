package com.behalf.delta.service;

import com.behalf.delta.entity.Store;
import com.behalf.delta.entity.StoreOrder;
import com.behalf.delta.entity.ProductInventory;
import java.util.List;

public interface StoreService {
    // Store methods
    Store createStore(Store store);
    List<Store> getAllStores(String country);

}
