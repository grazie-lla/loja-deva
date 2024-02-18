package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.repository.OrderRepository;
import com.tech.ada.java.lojadeva.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

}
