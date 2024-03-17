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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


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

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

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

        // Mock data
        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Existing Name");
        existingClient.setEmail("existing@example.com");
        existingClient.setCpf("123.456.789-00");
        // Add more fields as necessary

        Client clientRequest = new Client();
        clientRequest.setName("New Name");
        clientRequest.setEmail("new@example.com");
        clientRequest.setCpf("123.456.789-01");
        // Add more fields as necessary

        when(clientRepository.findById(id)).thenReturn(java.util.Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method to test
        Client updatedClient = clientService.updateClient(id, clientRequest);

        // Verify that the repository methods were called with the expected arguments
        verify(clientRepository).findById(id);
        verify(clientRepository).save(any(Client.class));

        // Assertions
        assertEquals(id, updatedClient.getId());
        assertEquals(clientRequest.getName(), updatedClient.getName());
        assertEquals(clientRequest.getEmail(), updatedClient.getEmail());
        assertEquals(clientRequest.getCpf(), updatedClient.getCpf());
        // Add more assertions for other fields as necessary
    }

    @Test
    void updateClientNotFoundTest() {


        Long id = 1L;

        when(clientRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(id, new Client());
        });

        verify(clientRepository).findById(id);

        verify(clientRepository, never()).save(any(Client.class));
    }
    @Test
    public void testUpdateClient_CpfMismatch() {

        Long id = 1L;
        String existingCpf = "123.456.789-00";
        String requestCpf = "123.456.789-01";

        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Nathalya Lucena");
        existingClient.setEmail("nathy@example.com");
        existingClient.setCpf(existingCpf);

        Client clientRequest = new Client();
        clientRequest.setName("Nathalya Lucena Vieira de Melo");
        clientRequest.setEmail("nathy@example.com");
        clientRequest.setCpf(requestCpf);

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));

        Client updatedClient = clientService.updateClient(id, clientRequest);

        verify(clientRepository).findById(id);

        verify(clientRepository, never()).save(any(Client.class));

        verify(clientService).validateClient(clientRequest);

        assertEquals(existingClient, updatedClient);
    }
    @Test
    void partialUpdateClientTest() {


        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Nathalya Lucena");
        existingClient.setEmail("nathy@example.com");
        existingClient.setCpf("123.456.789-00");
        existingClient.setAddress("Nathy Address");
        existingClient.setPostalCode("01234-567");
        existingClient.setPhoneNumber("(98)12345-6789");
        existingClient.setPassword("LojaDeva2024!");

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("Nathalya Lucena Vieira de Melo");
        clientRequest.setEmail("nathy@example.com");
        clientRequest.setCpf("123.456.789-01");
        clientRequest.setAddress("Nathy New Address");
        clientRequest.setPostalCode("01235-567");
        clientRequest.setPhoneNumber("(11)12345-6789");
        clientRequest.setPassword("LojaDeva2024*");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method to test
        Client updatedClient = clientService.partialUpdateClient(id, clientRequest);

        // Verify that the repository method findById was called with the expected argument
        verify(clientRepository).findById(id);

        // Verify that the repository method save was called with the updated client
        verify(clientRepository).save(existingClient);

        // Assertions
        assertEquals(existingClient.getId(), updatedClient.getId());
        assertEquals(clientRequest.getName(), updatedClient.getName());
        assertEquals(clientRequest.getEmail(), updatedClient.getEmail());
        assertEquals(clientRequest.getCpf(), updatedClient.getCpf());
        assertEquals(clientRequest.getAddress(), updatedClient.getAddress());
        assertEquals(clientRequest.getPostalCode(), updatedClient.getPostalCode());
        assertEquals(clientRequest.getPhoneNumber(), updatedClient.getPhoneNumber());
        assertEquals(clientRequest.getPassword(), updatedClient.getPassword());

    }
    @Test
    void partialUpdateClientNotFoundTest() {

        Long id = 1L;

        when(clientRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            clientService.partialUpdateClient(id, new ClientRequest());
        });
    }
    @Test
    public void testPartialUpdateClient_NoChanges() {

        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Nathalya Lucena");
        existingClient.setEmail("nathy@example.com");
        existingClient.setCpf("123.456.789-00");
        existingClient.setAddress("Nathy Address");
        existingClient.setPostalCode("01234-567");
        existingClient.setPhoneNumber("(98)12345-6789");
        existingClient.setPassword("LojaDeva2024!");

        ClientRequest clientRequest = new ClientRequest();

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));


        Client updatedClient = clientService.partialUpdateClient(id, clientRequest);

        verify(clientRepository).findById(id);

        verify(clientRepository, never()).save(existingClient);

        assertEquals(existingClient, updatedClient);
    }
    @Test
    public void testUpdateClient_NoChanges() {
        // Mock data
        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setName("Nathalya Lucena");
        existingClient.setEmail("nathy@example.com");
        existingClient.setCpf("123.456.789-00");
        existingClient.setAddress("Nathy Address");
        existingClient.setPostalCode("01234-567");
        existingClient.setPhoneNumber("(98)12345-6789");
        existingClient.setPassword("LojaDeva2024!");

        Client clientRequest = new Client();
        clientRequest.setName("Nathalya Lucena");
        clientRequest.setEmail("nathy@example.com");
        clientRequest.setCpf("123.456.789-00");
        clientRequest.setAddress("Nathy Address");
        clientRequest.setPostalCode("01234-567");
        clientRequest.setPhoneNumber("(98)12345-6789");
        clientRequest.setPassword("LojaDeva2024!");

        
        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));

        Client updatedClient = clientService.updateClient(id, clientRequest);

        verify(clientRepository).findById(id);

        verify(clientRepository, never()).save(existingClient);

        // Assertions
        assertEquals(existingClient, updatedClient);
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