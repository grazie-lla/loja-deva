package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.PaymentMethod;
import com.tech.ada.java.lojadeva.domain.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class OrderRequest {

    private Long clientId;
    private List<Product> products;
    private BigDecimal total;
    private String paymentMethod;

    public OrderRequest(Long clientId,
                        List<Product> products,
                        BigDecimal total,
                        String paymentMethod) {

        this.clientId = Objects.requireNonNull(clientId, "Client id is mandatory.");

        this.products = Objects.requireNonNull(products, "Products list is mandatory.");
        if (products.isEmpty()) {
            throw new IllegalArgumentException("Products list must not be empty.");
        }

        this.total = Objects.requireNonNull(total, "Order total is mandatory.");
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Order total must not be negative.");
        }

        this.paymentMethod = Objects.requireNonNull(paymentMethod, "Payment method is mandatory.");
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method.");
        }

    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return Arrays.stream(PaymentMethod.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(paymentMethod));
    }

}
