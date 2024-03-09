package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.dto.BasketItemRequest;
import com.tech.ada.java.lojadeva.dto.UpdateItemQuantityRequest;
import com.tech.ada.java.lojadeva.service.BasketItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BasketItemControllerTest {
    @Mock
    private BasketItemService basketItemService;

    @InjectMocks
    private BasketItemController basketItemController;
    private MockMvc mockMvc;

    private BasketItem basketItem1;
    private BasketItem basketItem2;
    private BasketItemRequest basketItemRequest;
    private UpdateItemQuantityRequest updateItemQuantityRequest;

    @BeforeEach
    void setUp() {
        ShoppingBasket shoppingBasket = new ShoppingBasket();

        basketItem1 = new BasketItem();
        basketItem1.setId(1L);
        basketItem1.setQuantity(2);
        basketItem1.setShoppingBasket(shoppingBasket);
        basketItem1.setProduct(new Product());

        basketItem2 = new BasketItem();
        basketItem2.setId(2L);
        basketItem2.setQuantity(3);
        basketItem2.setShoppingBasket(shoppingBasket);
        basketItem2.setProduct(new Product());

        basketItemRequest = new BasketItemRequest();
        basketItemRequest.setQuantity(4);
        basketItemRequest.setShoppingBasketId(1L);
        basketItemRequest.setProductId(1L);

        updateItemQuantityRequest = new UpdateItemQuantityRequest();
        updateItemQuantityRequest.setQuantity(5);

        mockMvc = MockMvcBuilders.standaloneSetup(basketItemController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAllItemsHttpRequest() throws Exception {
        List<BasketItem> expectedItems = Arrays.asList(basketItem1, basketItem2);
        when(basketItemService.findAllItems()).thenReturn(expectedItems);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/basket-item/items").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(expectedItems))).
                andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.[0].id", equalTo(1)));
        verify(basketItemService, times(1)).findAllItems();
    }

    @Test
    void findItemByIdHttpRequest() throws Exception {
        Long id = 1L;
        Optional<BasketItem> expectedItem = Optional.of(basketItem1);
        when(basketItemService.findItemById(id)).thenReturn(expectedItem);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/basket-item/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(expectedItem)))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.id", equalTo(1)));
        verify(basketItemService, times(1)).findItemById(id);
    }

    @Test
    void findItemByIdNotFoundHttpRequest() throws Exception {
        Long id = 3L;
        Optional<BasketItem> expectedItem = Optional.empty();
        when(basketItemService.findItemById(id)).thenReturn(expectedItem);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/basket-item/3").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(expectedItem))).
                        andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNotFound());
        verify(basketItemService, times(1)).findItemById(id);
    }

    @Test
    void findItemsByShoppingBasketIdHttpRequest() throws Exception {
        Long shoppingBasketId = 1L;
        List<BasketItem> expectedItems = Arrays.asList(basketItem1, basketItem2);
        when(basketItemService.findItemsByShoppingBasketId(shoppingBasketId)).thenReturn(expectedItems);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/basket-item/items/by-basket").
                        param("shoppingBasketId", "1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(expectedItems))).
                        andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.[0].id", equalTo(1)));
        verify(basketItemService, times(1)).findItemsByShoppingBasketId(shoppingBasketId);
    }

    @Test
    void createItemHttpRequest() throws Exception {
        when(basketItemService.createItem(Mockito.any())).thenReturn(basketItem1);

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/basket-item").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(basketItemRequest))).andExpect(status().isCreated());

        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.id", equalTo(1)));
        verify(basketItemService, times(1)).createItem(Mockito.any());
    }

    @Test
    void deleteItemHttpRequest() throws Exception {
        Long id = 1L;
        when(basketItemService.deleteItem(id)).thenReturn(true);

        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/basket-item/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(basketItem1)))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
        response.andExpect(content().string("Item deletado com sucesso!"));
        verify(basketItemService, times(1)).deleteItem(id);
    }

    @Test
    void deleteItemNotFoundHttpRequest() throws Exception {
        Long id = 3L;
        when(basketItemService.deleteItem(id)).thenReturn(false);

        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/basket-item/3").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(basketItem1)))
                        .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNotFound());
        verify(basketItemService, times(1)).deleteItem(id);
    }

    @Test
    void updateItemQuantityHttpRequest() throws Exception {
        Long id = 2L;
        BasketItem expectedItem = new BasketItem();
        expectedItem.setId(2L);
        expectedItem.setQuantity(5);
        expectedItem.setShoppingBasket(new ShoppingBasket());
        expectedItem.setProduct(new Product());
        when(basketItemService.updateItemQuantity(id, 5)).thenReturn(expectedItem);

        var response = mockMvc.perform(MockMvcRequestBuilders.patch("/basket-item/2").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(updateItemQuantityRequest)))
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.id", equalTo(2)));
        verify(basketItemService, times(1)).updateItemQuantity(id, 5);
    }

    @Test
    void updateItemQuantityNotFoundHttpRequest() throws Exception {
        Long id = 3L;
        when(basketItemService.updateItemQuantity(id, 5)).thenReturn(null);

        var response = mockMvc.perform(MockMvcRequestBuilders.patch("/basket-item/3").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(updateItemQuantityRequest)))
                        .andDo(MockMvcResultHandlers.print()).
                        andExpect(status().isNotFound());

        response.andExpect(status().isNotFound());
        verify(basketItemService, times(1)).updateItemQuantity(id, 5);
    }
}