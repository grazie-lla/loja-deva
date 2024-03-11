package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.service.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {
    @Mock
    private OrderItemService orderItemService;
    private OrderItem orderItem;
    private List<OrderItem> orderItemList;
    Order order;

    @InjectMocks
    private OrderItemController orderItemController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        order = new Order();
        order.setId(1L);
        orderItem = new OrderItem(1L, order, new Product(), 1);
        orderItemList = List.of(orderItem);
        order.setOrderItems(orderItemList);
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findOrderItemsByOrderIdHttpRequest() throws Exception {
        when(orderItemService.findOrderItemsByOrderId(Mockito.anyLong())).thenReturn(orderItemList);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/{orderId}/items", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        verify(orderItemService).findOrderItemsByOrderId(Mockito.anyLong());
        assertFalse(responseBody.isEmpty());
        assertEquals(asJsonString(orderItemList), responseBody);
    }

    @Test
    public void findOrderItemsByOrderIdNotFoundHttpRequest() throws Exception {
        List<OrderItem> emptyList = Collections.emptyList();
        when(orderItemService.findOrderItemsByOrderId(Mockito.anyLong())).thenReturn(emptyList);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/{orderId}/items", 123))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        verify(orderItemService).findOrderItemsByOrderId(Mockito.anyLong());
        assertFalse(responseBody.isEmpty());
        assertEquals(asJsonString(emptyList), responseBody);
    }

    @Test
    public void findOrderItemByIdHttpRequest() throws Exception {
        when(orderItemService.findOrderItemById(Mockito.any())).thenReturn(Optional.of(orderItem));

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/{orderId}/items/{id}", 1, 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        verify(orderItemService).findOrderItemById(Mockito.any());
        assertFalse(responseBody.isEmpty());
        assertEquals(asJsonString(orderItem), responseBody);
    }

    @Test
    public void findOrderItemByIdNotFoundHttpRequest() throws Exception {
        when(orderItemService.findOrderItemById(Mockito.any())).thenReturn(Optional.empty());

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/{orderId}/items/{id}", 1, 123))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        verify(orderItemService).findOrderItemById(Mockito.any());
        assertTrue(responseBody.isEmpty());
    }
}