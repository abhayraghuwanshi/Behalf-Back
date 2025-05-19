package com.behalf.store.web;

import com.behalf.store.model.dto.ProductPriceDTO;
import com.behalf.store.service.impl.StoreProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-prices")
public class StoreProductPriceController {

    @Autowired
    private StoreProductPriceService service;

    @PostMapping
    public ProductPriceDTO create(@RequestBody ProductPriceDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ProductPriceDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductPriceDTO getById(@PathVariable Long id) {
        return service.getById(id)
                .orElseThrow(() -> new RuntimeException("ProductPrice not found with id " + id));
    }

    @PutMapping("/{id}")
    public ProductPriceDTO update(@PathVariable Long id, @RequestBody ProductPriceDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
