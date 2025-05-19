package com.behalf.store.mapper;


import com.behalf.store.model.dto.ProductPriceDTO;
import com.behalf.store.model.Product;
import com.behalf.store.model.ProductPrice;
import com.behalf.store.model.Store;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceMapper {

    public ProductPriceDTO toDTO(ProductPrice entity) {
        ProductPriceDTO dto = new ProductPriceDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setStoreId(entity.getStore().getId());
        dto.setPrice(entity.getPrice());
        dto.setDiscount(entity.getDiscount());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setEffectiveTo(entity.getEffectiveTo());
        return dto;
    }

    public ProductPrice toEntity(ProductPriceDTO dto, Product product, Store store) {
        ProductPrice entity = new ProductPrice();
        entity.setId(dto.getId());
        entity.setProduct(product);
        entity.setStore(store);
        entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        entity.setCurrencyCode(dto.getCurrencyCode());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        return entity;
    }
}
