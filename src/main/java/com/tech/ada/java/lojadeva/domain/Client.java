package com.tech.ada.java.lojadeva.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório!")
    private String name;

    @NotBlank(message = "O email é obrigatório!")
    @Email (message = "Formato de email inválido.")
    private String email;

    @NotBlank(message = "O CPF é obrigatório!")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", message = "CPF inválido!")
    private String cpf;

    private String address;

    private String postalCode;

    private String phoneNumber;

    private String password;



    // Ana Luiza

    //@OneToMany(mappedBy = "client")
    //private List<ShoppingBasket> shoppingBaskets;
}
