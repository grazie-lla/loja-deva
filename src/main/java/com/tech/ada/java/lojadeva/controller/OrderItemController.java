package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/order")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItem> findOrderItemsByOrderId(@PathVariable Long orderId){
        return orderItemService.findOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{id}")
    public ResponseEntity<OrderItem> findOrderItemById(@PathVariable Long id, @PathVariable String orderId){
        Optional<OrderItem> orderItem = orderItemService.findOrderItemById(id);
        return orderItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
