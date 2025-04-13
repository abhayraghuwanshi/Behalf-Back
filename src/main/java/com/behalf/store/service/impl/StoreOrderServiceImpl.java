package com.behalf.store.service.impl;

import com.behalf.store.cons.OrderStatus;
import com.behalf.store.model.*;
import com.behalf.store.repo.*;
import com.behalf.store.service.IStoreOrderService;
import com.behalf.store.service.StoreOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreOrderServiceImpl implements IStoreOrderService {

    private final CartRepository cartRepository;
    private final ProductPriceRepository productPriceRepository;
    private final DiscountRepository discountRepository;
    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderItemRepository storeOrderItemRepository;

    @Override
    @Transactional
    public StoreOrder placeOrderFromCart(Long userId, String address, String country) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        StoreOrder order = new StoreOrder();
        order.setUserId(userId);
        order.setAddress(address);
        order.setCountry(country);
        order.setStatus(OrderStatus.PENDING);

        double total = 0.0;
        double totalDiscounted = 0.0;

        List<StoreOrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            Store store = cartItem.getStore();

            // Get latest price (filter by today’s date)
            ProductPrice price = productPriceRepository
                    .findTopByProductIdAndStoreIdAndEffectiveFromBeforeAndEffectiveToAfterOrderByEffectiveFromDesc(
                            product.getId(), store.getId(), LocalDate.now(), LocalDate.now())
                    .orElseThrow(() -> new RuntimeException("Price not available"));

            double pricePerUnit = price.getPrice();

            // Try to find active discount
            Discount discount = discountRepository
                    .findTopByProductIdAndStoreIdAndStartDateBeforeAndEndDateAfterAndIsActiveTrue(
                            product.getId(), store.getId(), LocalDate.now(), LocalDate.now())
                    .orElse(null);

            double discountedPrice = (discount != null && discount.getDiscountPrice() != null)
                    ? discount.getDiscountPrice().doubleValue()
                    : pricePerUnit;

            int qty = cartItem.getQuantity();
            double totalPrice = discountedPrice * qty;

            StoreOrderItem orderItem = new StoreOrderItem();
            orderItem.setOrder(order);  // mapped via @ManyToOne
            orderItem.setProduct(product);
            orderItem.setQuantity(qty);
            orderItem.setPricePerUnit(discountedPrice);
            orderItem.setTotalPrice(totalPrice);
            orderItems.add(orderItem);

            total += pricePerUnit * qty;
            totalDiscounted += totalPrice;
        }

        order.setPrice(total);
        order.setDiscountPrice(totalDiscounted);

        StoreOrder savedOrder = storeOrderRepository.save(order);
        for (StoreOrderItem item : orderItems) {
            item.setOrder(savedOrder);
        }
        storeOrderItemRepository.saveAll(orderItems);

        // Optionally: clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }
}
