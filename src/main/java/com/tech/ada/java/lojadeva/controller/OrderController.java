package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> generateOrder(@RequestBody OrderRequest orderRequest) {
        Order newOrder = orderService.generateOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

}
