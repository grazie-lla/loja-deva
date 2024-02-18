package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Long clientId;
    private List<Product> products;
    private BigDecimal total;
    private String paymentMethod;

}
