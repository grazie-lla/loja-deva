package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void generateOrderTest() {}

    @Test
    public void findAllOrdersTest() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orderList = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orderList);

        List<Order> result = orderService.findAllOrders();

        verify(orderRepository).findAll(); // verify that the findAll() method of the orderRepository mock object was called exactly once
        assertEquals(result.size(), 2);
        assertEquals(result, orderList);
    }

    @Test
    public void findOrderByIdTest() {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.findOrderById(orderId);

        verify(orderRepository).findById(orderId);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    public void findOrderByIdNotFoundTest() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.findOrderById(orderId);

        verify(orderRepository).findById(orderId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findOrdersByClientIdTest(){}

    @Test
    public void updateOrderTest() {}

    @Test
    public void deleteOrderByIdTest() {
        Long orderId = 1L;
        Order orderToDelete = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderToDelete));

        assertTrue(orderService.deleteOrderById(orderId));

        verify(orderRepository).delete(orderToDelete);
    }

    @Test
    public void deleteOrderByIdNotFoundTest() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertFalse(orderService.deleteOrderById(orderId));

        verify(orderRepository, never()).delete(any());
    }

    @Test
    public void isValidPaymentMethodTest() {}

    @Test
    public void emptyShoppingBasketTest() {}

    @Test
    public void isOrderCancelledTest() {}
}