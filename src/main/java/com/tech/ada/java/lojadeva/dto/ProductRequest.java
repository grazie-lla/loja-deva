package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.Product;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {
    private String name;

    private String description;

    private BigDecimal price;

    private Integer inventoryQuantity;

    private String category;


}
