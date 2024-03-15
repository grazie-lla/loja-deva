package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.UpdateProductDetailsRequest;
import com.tech.ada.java.lojadeva.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public OrderItemService(OrderItemRepository orderItemRepository, ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Optional<OrderItem> findOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> createOrderItemsFromBasketItems(Order order, List<BasketItem> basketItems) {
        if (basketItems.isEmpty()) {
            throw new IllegalArgumentException("O carrinho está vazio.");
        }

        for (BasketItem basketItem : basketItems) {
            if (!isProductQuantityAvailable(basketItem.getProduct(), basketItem.getQuantity())) {
                throw new IllegalArgumentException("A quantidade desejada não está disponível no estoque.");
            }

            OrderItem orderItem = OrderItem.fromBasketItemToOrderItem(order, basketItem);
            orderItemRepository.save(orderItem);
            removeProductQuantityFromInventory(orderItem.getProduct(), orderItem.getQuantity());
        }

        return orderItemRepository.findByOrderId(order.getId());
    }

    public void returnOrderItemsToInventory(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            returnProductQuantityToInventory(orderItem.getProduct(), orderItem.getQuantity());
        }
    }

    public boolean isProductQuantityAvailable(Product product, Integer quantity) {
        return quantity <= product.getInventoryQuantity();
    }

    public void removeProductQuantityFromInventory(Product product, Integer quantity) {
        Integer inventoryQuantityAfter = product.getInventoryQuantity() - quantity;

        UpdateProductDetailsRequest request =
                new UpdateProductDetailsRequest(null, null, inventoryQuantityAfter);

        productService.updateProductDetails(product.getId(), request);
    }

    public void returnProductQuantityToInventory(Product product, Integer quantity) {
        Integer inventoryQuantityAfter = product.getInventoryQuantity() + quantity;

        UpdateProductDetailsRequest request =
                new UpdateProductDetailsRequest(null, null, inventoryQuantityAfter);

        productService.updateProductDetails(product.getId(), request);
    }

}
