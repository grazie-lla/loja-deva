package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ShoppingBasketService shoppingBasketService;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private List<Client> clients;

    @BeforeEach
    void setup() {
        client = new Client();
        client.setId(1L);
        client.setName("Name");
        client.setEmail("name@gmail.com");
        client.setCpf("123.456.789-00");
        client.setAddress("ABC Street, 123");
        client.setPostalCode("12345-000");
        client.setPhoneNumber("(11)999999999");
        client.setPassword("password");

        ShoppingBasket basket = new ShoppingBasket();
        client.setShoppingBasket(basket);

        clients = List.of(client);
    }

    @Test
    void registerClientTest() {
    }

    @Test
    void updateClientTest() {
    }

    @Test
    void updateClientNotFoundTest() {
    }

    @Test
    void validateClientTest() {
    }

    @Test
    void validateClientWhenInvalidCPFTest() {
    }

    @Test
    void validateClientWhenCPFAlreadyExistsTest() {
    }

    @Test
    void partialUpdateClientTest() {
    }

    @Test
    void getClientByIdTest() {
        Long id = 1L;
        Client expectedClient = client;
        when(clientRepository.findById(id)).thenReturn(Optional.of(expectedClient));

        Client actualClient = clientService.getClientById(id);

        verify(clientRepository).findById(id);
        assertNotNull(actualClient);
        assertEquals(expectedClient, actualClient);
    }

    @Test
    void getClientByIdNotFoundTest() {
        Long nonExistingId = 123L;
        when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Client actualClient = clientService.getClientById(nonExistingId);

        verify(clientRepository).findById(nonExistingId);
        assertNull(actualClient);
    }

    @Test
    void deleteClientTest() {
        Long id = 1L;
        Client clientToDelete = client;
        when(clientRepository.findById(id)).thenReturn(Optional.of(clientToDelete));

        clientService.deleteClient(id);

        verify(clientRepository).delete(clientToDelete);
    }

    @Test
    void deleteClientWhenIdNotFoundTest() {
        Long nonExistingId = 123L;
        when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> clientService.deleteClient(nonExistingId));

        verify(clientRepository, never()).delete(any());
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void findAllClientsTest() {
        List<Client> expectedClients = clients;
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> actualClients = clientService.findAllClients();

        verify(clientRepository).findAll();
        assertEquals(expectedClients, actualClients);
    }
}