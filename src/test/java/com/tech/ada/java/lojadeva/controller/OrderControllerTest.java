package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.service.OrderService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;
    private Order order;
    private List<Order> orderList;
    private OrderRequest orderRequest;

    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        order = new Order();
        orderRequest = new OrderRequest(1L, "credit card");
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
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
    public void generateOrderHttpTest() throws Exception {
        when(orderService.generateOrder(Mockito.any())).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(order))).
                andExpect(status().isCreated());

        verify(orderService, times(1)).generateOrder(Mockito.any());
    }


    @Test
    public void findAllOrdersHttpTest() throws Exception {
        when(orderService.findAllOrders()).thenReturn(orderList);

        mockMvc.perform(MockMvcRequestBuilders.get("/order").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(order))).
                andDo(MockMvcResultHandlers.print());

        verify(orderService, times(1)).findAllOrders();
    }

    @Test
    public void findOrderByIdHttpTest() throws Exception {
        when(orderService.findOrderById(Mockito.any())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(order))).
                        andDo(MockMvcResultHandlers.print());

        verify(orderService,times(1)).findOrderById(Mockito.any());

    }
}