package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.ada.java.lojadeva.domain.Order;
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
import java.util.Collections;
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
    private UpdateOrderRequest updateOrderRequest;
    private static ObjectMapper objectMapper;

    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        order = new Order();
        order.setId(1L);
        order.setClientId(1L);
        order.setTotal(new BigDecimal("100.00"));
        order.setPaymentMethod(PaymentMethod.PIX);

        orderList = List.of(order);

        orderRequest = new OrderRequest(1L, "PIX");

        updateOrderRequest = new UpdateOrderRequest("ENVIADO");

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void generateOrderHttpTest() throws Exception {
        when(orderService.generateOrder(orderRequest)).thenReturn(order);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderRequest)))
                        .andExpect(status().isCreated())
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        verify(orderService).generateOrder(orderRequest);

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());

        Order responseOrder = objectMapper.readValue(responseBody, Order.class);
        assertEquals(order.getId(), responseOrder.getId());
    }

    @Test
    public void findAllOrdersHttpTest() throws Exception {
        when(orderService.findAllOrders()).thenReturn(orderList);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderList)))
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        verify(orderService).findAllOrders();

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());

        List<Order> responseOrderList = objectMapper.readValue(responseBody, new TypeReference<List<Order>>() {});
        assertEquals(orderList, responseOrderList);
    }

    @Test
    public void findOrderByIdHttpTest() throws Exception {
        when(orderService.findOrderById(Mockito.any())).thenReturn(Optional.of(order));

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        verify(orderService).findOrderById(Mockito.any());

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());

        Order responseOrder = objectMapper.readValue(responseBody, Order.class);
        assertEquals(order.getId(), responseOrder.getId());
    }

    @Test
    public void findOrderByIdNotFoundHttpTest() throws Exception {
        when(orderService.findOrderById(Mockito.any())).thenReturn(Optional.empty());

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isNotFound())
                        .andReturn();

        verify(orderService).findOrderById(Mockito.any());

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.isEmpty());
    }

    @Test
    public void findOrdersByClientIdHttpTest() throws Exception{
        when(orderService.findOrdersByClientId(Mockito.anyLong())).thenReturn(orderList);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order")
                        .param("clientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderList)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        verify(orderService).findOrdersByClientId(Mockito.anyLong());

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());

        List<Order> responseOrderList = objectMapper.readValue(responseBody, new TypeReference<List<Order>>() {});
        assertEquals(orderList, responseOrderList);
    }

    @Test
    public void findOrdersByClientIdNotFoundHttpTest() throws Exception{
        when(orderService.findOrdersByClientId(Mockito.anyLong())).thenReturn(Collections.emptyList());

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/order")
                        .param("clientId", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderList)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        verify(orderService).findOrdersByClientId(Mockito.anyLong());

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());

        List<Order> responseOrderList = objectMapper.readValue(responseBody, new TypeReference<List<Order>>() {});
        assertTrue(responseOrderList.isEmpty());
    }

    @Test
    public void updateOrderHttpTest() throws Exception{
        order.setStatus(Status.ENVIADO);
        when(orderService.updateOrder(Mockito.anyLong(), eq(updateOrderRequest))).thenReturn(ResponseEntity.of(Optional.of(order)));

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateOrderRequest)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        verify(orderService).updateOrder(Mockito.anyLong(), eq(updateOrderRequest));

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());

        Order responseOrder = objectMapper.readValue(responseBody, Order.class);
        assertEquals(order, responseOrder);
    }

    @Test
    void deleteOrderByIdHttpTest() throws Exception {
        when(orderService.deleteOrderById(anyLong())).thenReturn(true);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        verify(orderService).deleteOrderById(anyLong());

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());
        assertEquals("Pedido excluído com sucesso.", responseBody);
    }

    @Test
    void deleteOrderByIdNotFoundHttpTest() throws Exception {
        when(orderService.deleteOrderById(anyLong())).thenReturn(false);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isNotFound())
                        .andReturn();

        verify(orderService).deleteOrderById(anyLong());

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.isEmpty());
    }
}