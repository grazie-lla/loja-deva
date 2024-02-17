package com.tech.ada.java.lojadeva.domain.controller;

import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.domain.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> registerClient(@Valid @RequestBody Client client) {
        Client newClient = clientService.registerClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }
}

