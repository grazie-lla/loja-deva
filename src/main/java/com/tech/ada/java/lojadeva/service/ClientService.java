package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.dto.ClientRequest;
import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.repository.ClientRepository;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ShoppingBasketService shoppingBasketService;

    @Autowired
    public ClientService(ClientRepository clientRepository, ShoppingBasketService shoppingBasketService) {
        this.clientRepository = clientRepository;
        this.shoppingBasketService = shoppingBasketService;
    }

    public Client registerClient(@Valid Client client) {
        validateClient(client);

        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setClient(client);
        ShoppingBasket savedShoppingBasket = shoppingBasketService.createShoppingBasket(shoppingBasket);

        client.setShoppingBasket(savedShoppingBasket);
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, @Valid Client clientRequest) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (!existingClient.getCpf().equals(clientRequest.getCpf())) {
            validateClient(clientRequest);
        }

        existingClient.setName(clientRequest.getName());
        existingClient.setEmail(clientRequest.getEmail());
        existingClient.setCpf(clientRequest.getCpf());
        existingClient.setAddress(clientRequest.getAddress());
        existingClient.setPostalCode(clientRequest.getPostalCode());
        existingClient.setPhoneNumber(clientRequest.getPhoneNumber());
        existingClient.setPassword(clientRequest.getPassword());

        return clientRepository.save(existingClient);
    }

    private void validateClient(Client client) {

        if (!Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", client.getCpf())) {
            throw new IllegalArgumentException("CPF Inválido!");
        }

        Client isExistingClient = clientRepository.findByCpf(client.getCpf());
        if (isExistingClient != null) {
            throw new IllegalArgumentException("CPF já cadastrado!");
        }
    }

    public Client partialUpdateClient(Long id, @Valid ClientRequest clientRequest) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado!"));

        if (clientRequest.getName() != null) {
            existingClient.setName(clientRequest.getName());
        }
        if (clientRequest.getEmail() != null) {
            existingClient.setEmail(clientRequest.getEmail());
        }
        if (clientRequest.getCpf() != null) {
            existingClient.setCpf(clientRequest.getCpf());
        }
        if (clientRequest.getAddress() != null) {
            existingClient.setAddress(clientRequest.getAddress());
        }
        if (clientRequest.getPostalCode() != null) {
            existingClient.setPostalCode(clientRequest.getPostalCode());
        }
        if (clientRequest.getPhoneNumber() != null) {
            existingClient.setPhoneNumber(clientRequest.getPhoneNumber());
        }
        if (clientRequest.getPassword() != null) {
            existingClient.setPassword(clientRequest.getPassword());
        }

        return clientRepository.save(existingClient);

    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);

    }

    public void deleteClient(Long id) {

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        clientRepository.delete(existingClient);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }
    }


