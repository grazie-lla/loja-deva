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
            throw new IllegalArgumentException("A lista de itens está vazia.");
        }

        for (BasketItem basketItem : basketItems) {
            Product product = basketItem.getProduct();
            Integer quantity = basketItem.getQuantity();

            if (!isProductQuantityAvailable(product, quantity)) {
                throw new IllegalArgumentException("A quantidade do produto não está disponível.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);

            orderItemRepository.save(orderItem);
            updateProductInventory(product, quantity);
        }
        return orderItemRepository.findByOrderId(order.getId());
    }

    public boolean isProductQuantityAvailable(Product product, Integer quantity) {
        return quantity <= product.getInventoryQuantity();
    }

    public void updateProductInventory(Product product, Integer quantity) {
        Integer inventoryQuantityBefore = product.getInventoryQuantity();
        Integer inventoryQuantityAfter = inventoryQuantityBefore - quantity;

        UpdateProductDetailsRequest request =
                new UpdateProductDetailsRequest(null, null, inventoryQuantityAfter);

        productService.updateProductDetails(product.getId(), request);
    }
    
    // Todo: return items to repository when order is cancelled

}
