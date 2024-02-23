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
@Table(name = "shopping_basket")
public class ShoppingBasket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "shoppingBasket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketItem> basketItems;

    @Column(name = "total")
    private BigDecimal total;
}
