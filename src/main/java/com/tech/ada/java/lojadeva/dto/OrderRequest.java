package com.tech.ada.java.lojadeva.dto;

import com.tech.ada.java.lojadeva.domain.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Getter
@Setter
@EqualsAndHashCode
public class OrderRequest {

    @NotNull(message = "O id do carrinho é obrigatório.")
    @NotBlank(message = "O id do carrinho é obrigatório.")
    private Long basketId;

    @NotNull(message = "O método de pagamento é obrigatório.")
    @NotBlank(message = "O método de pagamento é obrigatório.")
    private String paymentMethod;

    public OrderRequest(Long basketId, String paymentMethod) {
        this.basketId = basketId;
        this.paymentMethod = paymentMethod;
    }

}
