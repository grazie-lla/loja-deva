package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.*;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderRequest;
import com.tech.ada.java.lojadeva.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ShoppingBasketService shoppingBasketService;
    private final BasketItemService basketItemService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemService orderItemService,
                        ShoppingBasketService shoppingBasketService,
                        BasketItemService basketItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.shoppingBasketService = shoppingBasketService;
        this.basketItemService = basketItemService;
    }

    @Transactional
    public Order generateOrder(OrderRequest orderRequest) {
        ShoppingBasket basket = shoppingBasketService
                .findBasketById(orderRequest.getBasketId())
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado."));

        if(!isValidPaymentMethod(orderRequest.getPaymentMethod())) {
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        Order order = new Order();
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemService.createOrderItemsFromBasketItems(order, basket.getBasketItems());

        order.setClientId(basket.getClient().getId());
        order.setOrderItems(orderItems);
        order.setTotal(basket.getTotal());
        order.setPaymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()));

        emptyShoppingBasket(basket);

        return orderRepository.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrdersByClientId(Long clientId) {
        return orderRepository.findOrderByClientId(clientId);
    }

    public ResponseEntity<Order> updateOrder(Long id, UpdateOrderRequest updateOrderRequest) {
        Optional<Order> orderToBeUpdated = findOrderById(id);

        if (orderToBeUpdated.isPresent()) {
            Order orderFound = orderToBeUpdated.get();
            updateOrderRequest.update(orderFound);
            Order orderUpdated = orderRepository.save(orderFound);

            if (isOrderCancelled(orderUpdated)) {
                orderItemService.returnOrderItemsToInventory(orderUpdated.getOrderItems());
            }

            return ResponseEntity.ok().body(orderUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Boolean deleteOrderById(Long id) {
        Optional<Order> orderToDelete = findOrderById(id);

        if (orderToDelete.isPresent()) {
            orderRepository.delete(orderToDelete.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidPaymentMethod(String paymentMethod) {
        return Arrays.stream(PaymentMethod.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(paymentMethod));
    }

    public void emptyShoppingBasket(ShoppingBasket basket) {
        for (BasketItem item : basket.getBasketItems()) {
            basketItemService.deleteItem(item.getId());
        }
        basket.setTotal(new BigDecimal(0));
    }

    public boolean isOrderCancelled(Order order) {
        return order.getStatus().equals(Status.CANCELADO);
    }

}
