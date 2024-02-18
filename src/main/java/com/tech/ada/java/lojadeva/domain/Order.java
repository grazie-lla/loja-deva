package com.tech.ada.java.lojadeva.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    @OneToMany
    private List<Product> products;

    private BigDecimal total;

    private String paymentMethod;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Order() {

    }

    public Order(Long clientId,
                 List<Product> products,
                 String paymentMethod) {
        this.clientId = clientId;
        this.products = products;
        this.paymentMethod = paymentMethod;
        this.status = "Pending";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
