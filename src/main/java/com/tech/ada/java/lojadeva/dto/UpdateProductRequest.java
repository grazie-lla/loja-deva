package com.tech.ada.java.lojadeva.dto;


import com.tech.ada.java.lojadeva.domain.Product;

import java.math.BigDecimal;
import java.util.Objects;

public record UpdateProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer inventoryQuantity,
        String category) {

    public UpdateProductRequest(String name, String description, BigDecimal price, Integer inventoryQuantity, String category){
        this.name = Objects.requireNonNull(name, "O nome do produto é obrigatório.");
        this.description = Objects.requireNonNull(description, "A descrição do produto é obrigatória.");
        this.price = Objects.requireNonNull(price, "O preço do produto é obrigatório.");
        this.inventoryQuantity = Objects.requireNonNull(inventoryQuantity, "A quantidade em estoque do produto é obrigatória.");
        this.category = Objects.requireNonNull(category, "A categoria do produto é obrigatória.");
    }

    public void updateProduct(Product product) {
        if (product != null) {
            product.setName(this.name());
            product.setDescription(this.description());
            product.setPrice(this.price());
            product.setInventoryQuantity(this.inventoryQuantity());
            product.setCategory(this.category());
        }
    }
}
