package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.PaymentMethod;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderDetailsRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderRequest;
import com.tech.ada.java.lojadeva.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    public Order generateOrder(OrderRequest orderRequest) {
        Order convertedOrder = modelMapper.map(orderRequest, Order.class);
        return orderRepository.save(convertedOrder);
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

    public ResponseEntity<String> deleteOrderById(Long id) {
        Optional<Order> orderToDelete = findOrderById(id);

        if (orderToDelete.isPresent()) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok().body("Order successfully deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
