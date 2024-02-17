package com.tech.ada.java.lojadeva.dto;

import java.math.BigDecimal;

public record UpdateProductDetailsRequest(
        String description,
        BigDecimal price,
        Integer inventoryQuantity) {
}
