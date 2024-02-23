package com.tech.ada.java.lojadeva.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BasketItemRequest {
    @NotBlank(message = "O shoppingBasketId é obrigatório!")
    @NotNull(message = "O shoppingBasketId é obrigatório!")
    private Long shoppingBasketId;

    @NotBlank(message = "O productId é obrigatório!")
    @NotNull(message = "O productId é obrigatório!")
    private Long productId;

    @NotBlank(message = "O quantity é obrigatório!")
    @NotNull(message = "O quantity é obrigatório!")
    @Min(value = 1, message = "É necessário pelo menos um produto!")
    private Integer quantity;
}
