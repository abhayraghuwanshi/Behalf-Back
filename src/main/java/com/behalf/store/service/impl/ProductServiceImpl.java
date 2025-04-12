package com.behalf.store.service.impl;


import com.behalf.store.model.Product;
import com.behalf.store.repo.ProductRepository;
import com.behalf.store.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + id);
        }

        updatedProduct.setId(id);
        return productRepository.save(updatedProduct);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
