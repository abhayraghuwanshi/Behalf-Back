package com.behalf.delta.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts(String name, Double price) {
        if(name != null && price != null) {
            return productRepository.findByNameContainingAndPriceLessThanEqual(name, price);
        } else if(name != null) {
            return productRepository.findByNameContainingAndPriceLessThanEqual(name, Double.MAX_VALUE);
        } else if(price != null) {
            return productRepository.findByNameContainingAndPriceLessThanEqual("", price);
        }
        return productRepository.findAll();
    }
}
