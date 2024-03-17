package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.domain.PaymentMethod;
import com.tech.ada.java.lojadeva.domain.Status;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderRequest;
import com.tech.ada.java.lojadeva.service.OrderService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;
    private Order order;
    private List<Order> orderList;

    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setClientId(1L);
        order.setOrderItems(List.of(orderItem));
        order.setTotal(new BigDecimal("100.00"));
        order.setPaymentMethod(PaymentMethod.PIX);

        orderList = List.of(order);

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
        OrderRequest orderRequest = new OrderRequest(1L, "PIX");

        when(orderService.generateOrder(orderRequest)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.orderItems", hasSize(1)))
                .andExpect(jsonPath("$.orderItems[0].id").value(1))
                .andExpect(jsonPath("$.total").value("100.0"))
                .andExpect(jsonPath("$.paymentMethod").value("PIX"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.createdAt").value(order.getCreatedAt().toString()))
                .andExpect(jsonPath("$.updatedAt").value(order.getUpdatedAt().toString()));
    }

    @Test
    public void findAllOrdersHttpTest() throws Exception {
        when(orderService.findAllOrders()).thenReturn(orderList);

        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void findOrderByIdHttpTest() throws Exception {
        when(orderService.findOrderById(Mockito.any())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1));
    }

    @Test
    public void findOrderByIdNotFoundHttpTest() throws Exception {
        when(orderService.findOrderById(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{id}", 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOrdersByClientIdHttpTest() throws Exception{
        when(orderService.findOrdersByClientId(Mockito.anyLong())).thenReturn(orderList);

        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .param("clientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void findOrdersByClientIdNotFoundHttpTest() throws Exception{
        when(orderService.findOrdersByClientId(Mockito.anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .param("clientId", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    public void updateOrderHttpTest() throws Exception{
        Order updatedUrder = order;
        updatedUrder.setStatus(Status.ENVIADO);
        updatedUrder.setUpdatedAt(LocalDateTime.now());

        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest("ENVIADO");

        when(orderService.updateOrder(Mockito.anyLong(), eq(updateOrderRequest)))
                .thenReturn(ResponseEntity.of(Optional.of(updatedUrder)));

        mockMvc.perform(MockMvcRequestBuilders.put("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateOrderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ENVIADO"))
                .andExpect(jsonPath("$.updatedAt").value(updatedUrder.getUpdatedAt().toString()));
    }

    @Test
    void deleteOrderByIdHttpTest() throws Exception {
        when(orderService.deleteOrderById(anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido excluído com sucesso."));
    }

    @Test
    void deleteOrderByIdNotFoundHttpTest() throws Exception {
        when(orderService.deleteOrderById(anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/order/{id}", 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }
}