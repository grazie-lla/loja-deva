package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderRequest;
import com.tech.ada.java.lojadeva.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public List<Order> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.findOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok().body(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(params = {"clientId"})
    public List<Order> findOrdersByClientId(@RequestParam Long clientId) {
        return orderService.findOrdersByClientId(clientId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequest updateOrderRequest) {
        ResponseEntity<Order> orderUpdated = orderService.updateOrder(id, updateOrderRequest);
        return ResponseEntity.status(orderUpdated.getStatusCode()).body(orderUpdated.getBody());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) {
        Boolean isOrderDeleted = orderService.deleteOrderById(id);
        if (isOrderDeleted) {
            return ResponseEntity.ok("Order successfully deleted.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
