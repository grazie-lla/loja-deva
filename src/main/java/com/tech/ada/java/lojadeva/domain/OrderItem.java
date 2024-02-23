package com.tech.ada.java.lojadeva.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public OrderItem(Product product, Integer quantity) {

    }

    public static List<OrderItem> toOrderItems(List<BasketItem> basketItems) {

        List<OrderItem> orderItems = new ArrayList<>();

        for (BasketItem basketItem : basketItems) {
            OrderItem orderItem = new OrderItem(basketItem.getProduct(),basketItem.getQuantity());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

}
