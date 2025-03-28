package com.behalf.delta.service;

import com.behalf.delta.entity.StoreProduct;
import com.behalf.delta.repo.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private StoreProductRepository storeProductRepository;

    public List<StoreProduct> getProducts(String name, Double price) {
        if(name != null && price != null) {
            return storeProductRepository.findByNameContainingAndPriceLessThanEqual(name, price);
        } else if(name != null) {
            return storeProductRepository.findByNameContainingAndPriceLessThanEqual(name, Double.MAX_VALUE);
        } else if(price != null) {
            return storeProductRepository.findByNameContainingAndPriceLessThanEqual("", price);
        }
        return storeProductRepository.findAll();
    }
}
