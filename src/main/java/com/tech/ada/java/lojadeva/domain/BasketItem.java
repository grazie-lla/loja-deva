package com.tech.ada.java.lojadeva.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "basket_item")
public class BasketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shopping_basket_id")
    private Long shoppingBasketId;

    @ManyToOne
    @JoinColumn(name = "shopping_basket_id", insertable = false, updatable = false)
    private ShoppingBasket shoppingBasket;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;
}