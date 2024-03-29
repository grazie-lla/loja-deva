package com.tech.ada.java.lojadeva.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private BigDecimal total;

    private PaymentMethod paymentMethod;

    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;

    public Order() {
        this.status = Status.PENDENTE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(Long clientId,
                 List<OrderItem> orderItems,
                 BigDecimal total,
                 PaymentMethod paymentMethod) {
        this.clientId = clientId;
        this.orderItems = orderItems;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.status = Status.PENDENTE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @JsonIgnore
    public boolean isUpdatable() {
        return !this.getStatus().equals(Status.CANCELADO) && !this.getStatus().equals(Status.ENTREGUE);
    }

}
