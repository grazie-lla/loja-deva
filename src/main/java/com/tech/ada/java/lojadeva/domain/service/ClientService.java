package com.tech.ada.java.lojadeva.domain.service;

import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.regex.Pattern;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client registerClient(@Valid Client client) {

        validateClient(client);
        return clientRepository.save(client);
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
}
