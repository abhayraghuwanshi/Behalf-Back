package com.behalf.store.web;

import com.behalf.delta.service.FileService;
import com.behalf.store.model.Product;
import com.behalf.store.service.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StoreProductController {

    private final StoreProductService storeProductService;

    private final FileService fileService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        List<String> imageUrls = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            imageUrls = fileService.uploadMultiple("store", images);
        }

        product.setImageUrls(imageUrls);
        Product created = storeProductService.createProduct(product);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(storeProductService.getProductById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(storeProductService.getAllProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(storeProductService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        storeProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
