package com.behalf.store.web;


import com.behalf.store.model.dto.ProductViewResponse;
import com.behalf.store.service.ProductViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-view")
public class ProductViewController {

    private final ProductViewService productViewService;

    @GetMapping("/by-country")
    public ResponseEntity<List<ProductViewResponse>> getProductsByCountry(@RequestParam String country) {
        return ResponseEntity.ok(productViewService.getProductsForCountry(country));
    }
}
