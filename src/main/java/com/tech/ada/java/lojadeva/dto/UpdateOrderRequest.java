package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.Status;

import java.util.Arrays;
import java.util.Objects;

public record UpdateOrderRequest (String status) {

    public UpdateOrderRequest(String status) {

        this.status = Objects.requireNonNull(status, "O status do pedido é obrigatório.");
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Status do pedido inválido.");
        }

    }

    public void update(Order order) {
        if (!order.isUpdatable()) {
            throw new IllegalArgumentException("Não é possível alterar o pedido.");
        }
        order.setStatus(Status.valueOf(this.status));
    }

    public boolean isValidStatus(String status) {
        return Arrays.stream(Status.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(status));
    }

}
