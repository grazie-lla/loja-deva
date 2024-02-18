package com.tech.ada.java.lojadeva.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer inventoryQuantity;

    private String category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BasketItem> basketItems;

    public Product(String name,
                   String description,
                   BigDecimal price,
                   Integer inventoryQuantity,
                   String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventoryQuantity = inventoryQuantity;
        this.category = category;
    }

    public Product() {

    }
}
