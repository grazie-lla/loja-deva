package com.tech.ada.java.lojadeva.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ShoppingBasketRequest {

    @NotBlank(message = "O clientId é obrigatório!")
    @NotNull(message = "O clientId é obrigatório!")
    private Long clientId;

    @NotBlank(message = "O productId é obrigatório!")
    @NotNull(message = "O productId é obrigatório!")
    private Long productId;

    @NotBlank(message = "O productQuantity é obrigatório!")
    @NotNull(message = "O productQuantity é obrigatório!")
    @Min(value = 1, message = "É necessário pelo menos um produto!")
    private Integer productQuantity;

    private String address;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido.")
    private String postalCode;






}
