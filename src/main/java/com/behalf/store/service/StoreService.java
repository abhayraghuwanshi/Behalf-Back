package com.behalf.store.service;

import com.behalf.store.model.Store;

import java.util.List;

public interface StoreService {
    // Store methods
    Store createStore(Store store);
    List<Store> getAllStores(String country);

}
