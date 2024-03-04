package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.ada.java.lojadeva.domain.OrderItem;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {
    @Mock
    private OrderItemService orderItemService;
    private OrderItem orderItem;
    private List<OrderItem> orderItemList;

    @InjectMocks
    private OrderItemController orderItemController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        orderItem = new OrderItem();
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

        mockMvc.perform(MockMvcRequestBuilders.get("/order/1/items").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(orderItemList)))
                .andDo(MockMvcResultHandlers.print());
        verify(orderItemService, times(1)).findOrderItemsByOrderId(Mockito.anyLong());
    }

    @Test
    public void findOrderItemByIdHttpRequest() throws Exception {
        when(orderItemService.findOrderItemById(Mockito.any())).thenReturn(Optional.of(orderItem));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/1/items/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(orderItem))).
                andDo(MockMvcResultHandlers.print());

        verify(orderItemService,times(1)).findOrderItemById(Mockito.any());

    }


}