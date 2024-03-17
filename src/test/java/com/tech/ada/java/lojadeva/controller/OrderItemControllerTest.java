package com.tech.ada.java.lojadeva.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
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

        Product product = new Product();
        product.setId(1L);

        orderItem = new OrderItem(1L, order, product, 1);

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

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{orderId}/items", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void findOrderItemsByOrderIdNotFoundHttpRequest() throws Exception {
        List<OrderItem> emptyList = Collections.emptyList();
        when(orderItemService.findOrderItemsByOrderId(Mockito.anyLong())).thenReturn(emptyList);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{orderId}/items", 123))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    public void findOrderItemByIdHttpRequest() throws Exception {
        when(orderItemService.findOrderItemById(Mockito.any())).thenReturn(Optional.of(orderItem));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{orderId}/items/{id}", 1, 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product.id").value(1))
                .andExpect(jsonPath("$.quantity").value(1));
    }

    @Test
    public void findOrderItemByIdNotFoundHttpRequest() throws Exception {
        when(orderItemService.findOrderItemById(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{orderId}/items/{id}", 1, 123))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }
}