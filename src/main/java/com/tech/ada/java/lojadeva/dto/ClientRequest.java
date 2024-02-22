package com.tech.ada.java.lojadeva.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {

        private Long id;
        private String name;
        private String email;
        private String cpf;
        private String address;
        private String postalCode;
        private String phoneNumber;
        private String password;


    public ClientRequest() {

    }
}

