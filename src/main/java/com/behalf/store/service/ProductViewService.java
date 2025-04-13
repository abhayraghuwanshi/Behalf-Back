// ProductViewService.java
package com.behalf.store.service;

import com.behalf.store.model.*;
import com.behalf.store.model.dto.ProductViewResponse;
import com.behalf.store.repo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductViewService {

    private final StoreRepository storeRepository;
    private final ProductPriceRepository priceRepository;
    private final ProductInventoryRepository inventoryRepository;
    private final DiscountRepository discountRepository;

    public List<ProductViewResponse> getProductsForCountry(String country) {
        List<Store> stores = storeRepository.findAllByCountry(country);
        List<Long> storeIds = stores.stream().map(Store::getId).toList();

        LocalDate today = LocalDate.now();

        List<ProductPrice> prices = priceRepository
                .findByStoreIdInAndEffectiveFromBeforeAndEffectiveToAfter(storeIds, today, today);

        List<ProductInventory> inventories = inventoryRepository.findByStoreIdIn(storeIds);

        // Load discounts (optional by store)
        List<Discount> storeDiscounts = discountRepository
                .findByStoreIdInAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(storeIds, today, today);

        Map<String, Discount> discountMap = storeDiscounts.stream()
                .collect(Collectors.toMap(
                        d -> d.getStore().getId() + "_" + d.getProduct().getId(),
                        d -> d,
                        (a, b) -> a  // pick any if multiple
                ));

        // Fall back to global product-wide discounts
        Set<Long> productIds = prices.stream().map(p -> p.getProduct().getId()).collect(Collectors.toSet());
        List<Discount> globalDiscounts = discountRepository
                .findByProductIdInAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
                        new ArrayList<>(productIds), today, today);

        Map<Long, Discount> globalDiscountMap = globalDiscounts.stream()
                .filter(d -> d.getStore() == null)  // only global ones
                .collect(Collectors.toMap(
                        d -> d.getProduct().getId(),
                        d -> d,
                        (a, b) -> a
                ));

        Map<String, ProductInventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        inv -> inv.getStore().getId() + "_" + inv.getProduct().getId(),
                        inv -> inv,
                        (a, b) -> a
                ));

        List<ProductViewResponse> responses = new ArrayList<>();

        for (ProductPrice price : prices) {
            Product product = price.getProduct();
            Store store = price.getStore();
            String key = store.getId() + "_" + product.getId();
            ProductInventory inventory = inventoryMap.get(key);
            if (inventory == null) continue;

            Double originalPrice = price.getPrice();
            BigDecimal finalPrice;

            // Store-specific discount preferred
            Discount discount = discountMap.get(key);
            if (discount == null) {
                discount = globalDiscountMap.get(product.getId());
            }

            if (discount != null) {
                finalPrice = discount.getDiscountPrice();
            } else {
                finalPrice = BigDecimal.valueOf(originalPrice); // No discount
            }

            Double discountPercentage = null;
            if (discount != null && discount.getDiscountPrice() != null) {
                discountPercentage = 100.0 * (originalPrice - discount.getDiscountPrice().doubleValue()) / originalPrice;
            }

            responses.add(ProductViewResponse.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .brand(product.getBrand())
                    .imageUrls(product.getImageUrls())
                    .currency(price.getCurrencyCode())
                    .originalPrice(originalPrice)
                    .discount(discountPercentage)
                    .finalPrice(finalPrice.doubleValue())
                    .outOfStock(inventory.getQuantity() <= 0)
                    .quantityAvailable(inventory.getQuantity())
                    .storeName(store.getName())
                    .storeLocation(store.getCity() + ", " + store.getCountry())
                            .storeId(store.getId())
                    .build());
        }

        return responses;
    }
}
