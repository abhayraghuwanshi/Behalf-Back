package com.behalf.store.service;


import com.behalf.store.model.Product;

import java.util.List;

public interface StoreProductService {
    Product createProduct(Product product);
    Product updateProduct(Long id, Product updatedProduct);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    void deleteProduct(Long id);
}
