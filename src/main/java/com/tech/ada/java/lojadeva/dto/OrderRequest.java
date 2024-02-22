package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.*;
import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

@Data
public class OrderRequest {

    private Long shoppingBasketId;
    private String paymentMethod;

    public OrderRequest(Long shoppingBasketId, String paymentMethod) {

        this.shoppingBasketId = shoppingBasketId;

        this.paymentMethod = Objects.requireNonNull(paymentMethod, "O método de pagamento é obrigatório.");
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Método de pagamento inválido.");
        }

    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return Arrays.stream(PaymentMethod.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(paymentMethod));
    }

    public Order toEntity() {

        //ShoppingBasket basket = e
        Order order = new Order();

        /*order.setClientId(basket.getClient().getId());
        order.setOrderItems(OrderItem.toOrderItems(basket.getBasketItems()));
        order.setTotal(basket.getTotal());
        order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));*/

        return order;
    }

}
