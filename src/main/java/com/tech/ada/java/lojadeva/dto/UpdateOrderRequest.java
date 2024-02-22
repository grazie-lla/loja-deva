package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.PaymentMethod;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record UpdateOrderRequest (
        String status
) {

    public UpdateOrderRequest(String status) {

        this.status = Objects.requireNonNull(status, "Order status is mandatory.");
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid order status.");
        }

    }

    public void update(Order order) {
        order.setStatus(Status.valueOf(this.status));
    }

    public boolean isValidStatus(String status) {
        return Arrays.stream(Status.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(status));
    }

}
