package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.dto.ClientRequest;
import com.tech.ada.java.lojadeva.dto.ClientResponse;
import com.tech.ada.java.lojadeva.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> registerClient(@Valid @RequestBody ClientRequest clientRequest) {
        Client client = convertToClient(clientRequest);
        Client newClient = clientService.registerClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToClientResponse(newClient));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @Valid @RequestBody Client clientRequest) {
        Client updatedClient = clientService.updateClient(id, clientRequest);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Client> partialUpdateClient(@PathVariable Long id, @Valid @RequestBody ClientRequest clientRequest) {
        Client updatedClient = clientService.partialUpdateClient(id, clientRequest);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable("id") Long id) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(convertToClientResponse(client));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    private ClientRequest convertToClientRequest(Client client) {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName(client.getName());
        clientRequest.setEmail(client.getEmail());
        clientRequest.setCpf(client.getCpf());
        clientRequest.setAddress(client.getAddress());
        clientRequest.setPostalCode(client.getPostalCode());
        clientRequest.setPhoneNumber(client.getPhoneNumber());
        clientRequest.setPassword(client.getPassword());
        return clientRequest;
    }

    private ClientResponse convertToClientResponse(Client client) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(client.getId());
        clientResponse.setName(client.getName());
        clientResponse.setEmail(client.getEmail());
        clientResponse.setCpf(client.getCpf());
        clientResponse.setAddress(client.getAddress());
        clientResponse.setPostalCode(client.getPostalCode());
        clientResponse.setPhoneNumber(client.getPhoneNumber());
//        clientResponse.setPassword(clientResponse.getPassword());

        return clientResponse;
    }

    private Client convertToClient(ClientRequest clientRequest) {
        Client client = new Client();
        client.setName(clientRequest.getName());
        client.setEmail(clientRequest.getEmail());
        client.setCpf(clientRequest.getCpf());
        client.setAddress(clientRequest.getAddress());
        client.setPostalCode(clientRequest.getPostalCode());
        client.setPhoneNumber(clientRequest.getPhoneNumber());
        client.setPassword(clientRequest.getPassword());
        return client;
    }
}