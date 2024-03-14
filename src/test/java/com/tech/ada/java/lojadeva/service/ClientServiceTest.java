package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.dto.ClientRequest;
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
    private ShoppingBasket basket;

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

        basket = new ShoppingBasket();
        basket.setClient(client);

        clients = List.of(client);
    }

    @Test
    void registerClientTest() {
        Client expectedClient = client;
        ShoppingBasket expectedBasket = basket;

        when(shoppingBasketService.createShoppingBasket(any(ShoppingBasket.class))).thenReturn(expectedBasket);
        when(clientRepository.save(any(Client.class))).thenReturn(expectedClient);

        Client actualClient = clientService.registerClient(expectedClient);

        assertNotNull(actualClient);
        assertSame(expectedClient, actualClient);
        assertSame(expectedBasket.getClient(), actualClient);
        assertSame(expectedBasket, actualClient.getShoppingBasket());
        verify(clientRepository).save(expectedClient);
    }

    @Test
    void registerClientWithInvalidCPFTest() {
        Client invalidClient = new Client();
        invalidClient.setCpf("123");

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> clientService.registerClient(invalidClient));

        assertEquals("CPF Inválido!", exception.getMessage());
        verify(shoppingBasketService, never()).createShoppingBasket(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void registerClientWhenCPFAlreadyExistsTest() {
        Client duplicateClient = new Client();
        duplicateClient.setCpf("123.456.789-00");

        when(clientRepository.findByCpf(duplicateClient.getCpf())).thenReturn(new Client());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> clientService.registerClient(duplicateClient));

        assertEquals("CPF já cadastrado!", exception.getMessage());
        verify(shoppingBasketService, never()).createShoppingBasket(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientTest() {
    }

    @Test
    void updateClientNotFoundTest() {
    }

    @Test
    void partialUpdateClientTest() {
        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Existing Name");
        existingClient.setEmail("existing@example.com");

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("New Name");
        clientRequest.setEmail("new@example.com");
        clientRequest.setCpf("000.000.000-00");
        clientRequest.setAddress("R teste");
        clientRequest.setPostalCode("000000000");
        clientRequest.setPhoneNumber("11999999999");
        clientRequest.setPassword("TesteSenha@123");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        Client updatedClient = clientService.partialUpdateClient(id, clientRequest);

        assertEquals("New Name", updatedClient.getName());
        assertEquals("new@example.com", updatedClient.getEmail());
        assertEquals("000.000.000-00", updatedClient.getCpf());
        assertEquals("R teste", updatedClient.getAddress());
        assertEquals("000000000", updatedClient.getPostalCode());
        assertEquals("11999999999", updatedClient.getPhoneNumber());
        assertEquals("TesteSenha@123", updatedClient.getPassword());
        verify(clientRepository).save(existingClient);
    }

    @Test
    void partialUpdateClientTestWhenNullValues() {
        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Existing Name");
        existingClient.setEmail("existing@example.com");

        ClientRequest clientRequest = new ClientRequest();

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        Client updatedClient = clientService.partialUpdateClient(id, clientRequest);

        assertEquals("Existing Name", updatedClient.getName());
        assertEquals("existing@example.com", updatedClient.getEmail());
    }

    @Test
    void partialUpdateClientTestWhenClientNotFound() {
        Long nonExistingId = 5L;
        when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> clientService.partialUpdateClient(nonExistingId, any()));

        verify(clientRepository, never()).delete(any());
        assertEquals("Cliente não encontrado!", exception.getMessage());
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