package com.behalf.store.service.impl;


import com.behalf.store.model.ProductPrice;
import com.behalf.store.repo.ProductPriceRepository;
import com.behalf.store.repo.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.behalf.store.model.dto.ProductPriceDTO;
import com.behalf.store.mapper.ProductPriceMapper;
import com.behalf.store.model.Product;
import com.behalf.store.model.Store;
import com.behalf.store.repo.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductPriceService {

    @Autowired private ProductPriceRepository repository;
    @Autowired private ProductRepository productRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private ProductPriceMapper mapper;

    public ProductPriceDTO create(ProductPriceDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        ProductPrice saved = repository.save(mapper.toEntity(dto, product, store));
        return mapper.toDTO(saved);
    }

    public List<ProductPriceDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductPriceDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    public ProductPriceDTO update(Long id, ProductPriceDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return repository.findById(id)
                .map(existing -> {
                    ProductPrice updated = mapper.toEntity(dto, product, store);
                    updated.setId(id);
                    return mapper.toDTO(repository.save(updated));
                }).orElseThrow(() -> new RuntimeException("ProductPrice not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
