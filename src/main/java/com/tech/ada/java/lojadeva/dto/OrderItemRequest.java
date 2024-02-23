package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    Product product;
    Integer quantity;

    public OrderItem toEntity() {
        return new OrderItem(product, quantity);
    }

}
