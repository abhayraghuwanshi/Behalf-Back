package com.behalf.store.web;


import com.behalf.store.model.dto.ProductViewResponse;
import com.behalf.store.service.impl.StoreProductCatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/api/product-view")
public class StoreProductCatalogueController {

    private final StoreProductCatalogueService productViewService;

    @GetMapping("/by-country")
    public ResponseEntity<List<ProductViewResponse>> getProductsByCountry(@RequestParam String country) {
        return ResponseEntity.ok(productViewService.getProductsForCountry(country));
    }

    @GetMapping()
    public ResponseEntity<ProductViewResponse> getProductsById(@RequestParam Long id) {
        return ResponseEntity.ok(productViewService.getProductViewById(id));
    }
}
