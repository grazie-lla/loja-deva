package com.tech.ada.java.lojadeva.dto;

import java.math.BigDecimal;
import java.util.Objects;

public record UpdateProductDetailsRequest(
        String description,
        BigDecimal price,
        Integer inventoryQuantity) {

    public UpdateProductDetailsRequest(String description, BigDecimal price, Integer inventoryQuantity){
        this.description = description;

        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
        }
        this.price = price;

        if (inventoryQuantity != null && inventoryQuantity < 0) {
            throw new IllegalArgumentException("A quantidade em estoque do produto não pode ser negativa.");
        }
        this.inventoryQuantity = inventoryQuantity;
    }
}
