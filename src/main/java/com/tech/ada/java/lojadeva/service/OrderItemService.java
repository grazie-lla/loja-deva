package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
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
            if (!isProductAvailable(basketItem.getProduct())) {
                throw new IllegalArgumentException("Produto fora de estoque.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(basketItem.getProduct());
            orderItem.setQuantity(basketItem.getQuantity());

            orderItemRepository.save(orderItem);
        }
        return orderItemRepository.findByOrderId(order.getId());
    }

    public boolean isProductAvailable(Product product) {
        return product.getInventoryQuantity() > 0;
    }

}
