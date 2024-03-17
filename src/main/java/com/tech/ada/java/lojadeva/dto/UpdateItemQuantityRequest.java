package com.tech.ada.java.lojadeva.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemQuantityRequest {
    @NotBlank(message = "O quantity é obrigatório!")
    @NotNull(message = "O quantity é obrigatório!")
    @Min(value = 1, message = "É necessário pelo menos um produto!")
    private Integer quantity;
}
