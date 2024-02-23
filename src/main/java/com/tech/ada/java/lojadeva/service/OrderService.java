package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.*;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderRequest;
import com.tech.ada.java.lojadeva.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingBasketService shoppingBasketService;
    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository,
                        ShoppingBasketService shoppingBasketService,
                        OrderItemService orderItemService,
                        ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.shoppingBasketService = shoppingBasketService;
        this.orderItemService = orderItemService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Order generateOrder(OrderRequest orderRequest) {
        ShoppingBasket basket = shoppingBasketService
                .findBasketById(orderRequest.getBasketId())
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado."));

        Order order = new Order();
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemService.createOrderItemsFromBasketItems(order, basket.getBasketItems());

        order.setClientId(basket.getClient().getId());
        order.setOrderItems(orderItems);
        order.setTotal(basket.getTotal());
        validatePaymentMethod(orderRequest.getPaymentMethod());
        order.setPaymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()));

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

            return ResponseEntity.ok().body(orderUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Boolean deleteOrderById(Long id) {
        Optional<Order> orderToDelete = findOrderById(id);

        if (orderToDelete.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private void validatePaymentMethod(String paymentMethod) {
        boolean isValid = Arrays.stream(PaymentMethod.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(paymentMethod));

        if(!isValid) {
            throw new IllegalArgumentException("Método de pagamento inválido");
        }
    }

}
