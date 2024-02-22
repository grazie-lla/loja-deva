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

        this.clientId = Objects.requireNonNull(clientId, "O id do cliente é obrigatório.");

        this.products = Objects.requireNonNull(products, "A lista de produtos é obrigatória.");
        if (products.isEmpty()) {
            throw new IllegalArgumentException("A lista de produtos não pode estar vazia.");
        }

        this.total = Objects.requireNonNull(total, "O valor total do pedido é obrigatório.");
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor total do pedido não pode ser negativo.");
        }

        this.paymentMethod = Objects.requireNonNull(paymentMethod, "O método de pagamento é obrigatório.");
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Método de pagamento inválido.");
        }

    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return Arrays.stream(PaymentMethod.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(paymentMethod));
    }

}
