package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record UpdateOrderRequest (
        Long clientId,
        List<Product> products,
        BigDecimal total,
        String paymentMethod,
        String status

) {

    public UpdateOrderRequest(Long clientId,
                              List<Product> products,
                              BigDecimal total,
                              String paymentMethod,
                              String status) {
        this.clientId = Objects.requireNonNull(clientId, "Client id is mandatory.");
        //this.products = Objects.requireNonNull(products, "Products list is mandatory.");
        this.products = products;
        this.total = Objects.requireNonNull(total, "Order total is mandatory.");
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "Payment method is mandatory.");
        this.status = Objects.requireNonNull(status, "Order status is mandatory.");
    }

    public void update(Order order) {
        order.setClientId(this.clientId);
        order.setProducts(this.products);
        order.setTotal(this.total);
        order.setPaymentMethod(this.paymentMethod);
        order.setStatus(this.status);
        order.setUpdatedAt(LocalDateTime.now());
    }
}
