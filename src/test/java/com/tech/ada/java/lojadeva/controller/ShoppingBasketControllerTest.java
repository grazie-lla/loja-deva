package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.service.ShoppingBasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShoppingBasketControllerTest {
    @Mock
    private ShoppingBasketService shoppingBasketService;
    private ShoppingBasket shoppingBasket;

    @InjectMocks
    private ShoppingBasketController shoppingBasketController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        shoppingBasket = new ShoppingBasket();
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingBasketController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findBasketById() throws Exception {
        when(shoppingBasketService.findBasketById(Mockito.any())).thenReturn(Optional.of(shoppingBasket));

        mockMvc.perform(MockMvcRequestBuilders.get("/shopping-basket/1").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(shoppingBasket))).andDo(MockMvcResultHandlers.print());

        verify(shoppingBasketService, times(1)).findBasketById(Mockito.any());
    }

}