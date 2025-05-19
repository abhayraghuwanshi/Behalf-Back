package com.behalf.store.service.impl;

import com.behalf.store.model.Store;
import com.behalf.store.repo.StoreRepository;
import com.behalf.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public List<Store> getAllStores(String country) {
        return storeRepository.findAllByCountry(country);
    }
}
