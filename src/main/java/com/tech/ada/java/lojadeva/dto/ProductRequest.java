package com.tech.ada.java.lojadeva.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class ProductRequest {
    private String name;

    private String description;

    private BigDecimal price;

    private Integer inventoryQuantity;

    private String category;

    public ProductRequest(String name, String description, BigDecimal price, Integer inventoryQuantity, String category){
        this.name = Objects.requireNonNull(name, "O nome do produto é obrigatório.");
        this.description = Objects.requireNonNull(description, "A descrição do produto é obrigatória.");
        this.price = Objects.requireNonNull(price, "O preço do produto é obrigatório.");
        this.inventoryQuantity = Objects.requireNonNull(inventoryQuantity, "A quantidade em estoque do produto é obrigatória.");
        this.category = Objects.requireNonNull(category, "A categoria do produto é obrigatória.");
    }

}
