package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.dto.ClientRequest;
import com.tech.ada.java.lojadeva.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    @Mock
    private ClientService clientService;
    private ClientRequest clientRequest;
    private Client client;

    private List<Client> clientList;

    @InjectMocks
    private ClientController clientController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        clientRequest = new ClientRequest();
        client = new Client();
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerClientHttpTest() throws Exception {
        when(clientService.registerClient(Mockito.any())).thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.post("/clients").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(clientRequest))).andExpect(status().isCreated());

        verify(clientService, times(1)).registerClient(Mockito.any());
    }

    @Test
    public void getClientByIdHttpRequest() throws Exception {
        when(clientService.getClientById(Mockito.any())).thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/1").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(client))).
        andDo(MockMvcResultHandlers.print());

        verify(clientService, times(1)).getClientById(Mockito.any());
    }
    @Test
    public void getClientByIdNotFoundHttpRequest() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getClientById(1L);
    }
    @Test
    public void updateClientHttpTest() throws Exception {
        when(clientService.updateClient(Mockito.any(), Mockito.any(Client.class)))
                .thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.put("/clients/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(client)))
                .andDo(MockMvcResultHandlers.print());

        verify(clientService, times(1)).updateClient(Mockito.any(),  Mockito.any(Client.class));
    }
    @Test
    public void partialUpdateClientHttpTest() throws Exception {
        when(clientService.partialUpdateClient(Mockito.any(), Mockito.any(ClientRequest.class)))
                .thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.patch("/clients/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(client)))
                .andDo(MockMvcResultHandlers.print());

        verify(clientService, times(1)).partialUpdateClient(Mockito.any(),  Mockito.any(ClientRequest.class));
    }
    @Test
    public void findAllClientsHttpRequest() throws Exception {
        when(clientService.findAllClients()).thenReturn(clientList);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(client))).
                andDo(MockMvcResultHandlers.print());

        verify(clientService, times(1)).findAllClients();
    }
    @Test
    public void deleteClientHttpRequest() throws Exception {
        doNothing().when(clientService).deleteClient(Mockito.any());


        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(client)))
                .andDo(MockMvcResultHandlers.print());

        verify(clientService, times(1)).deleteClient(Mockito.any());
    }
}