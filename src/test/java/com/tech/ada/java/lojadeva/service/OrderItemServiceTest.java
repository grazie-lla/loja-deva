package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Order;
import com.tech.ada.java.lojadeva.domain.OrderItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ProductService productService;

    private OrderItem orderItem;
    private List<OrderItem> orderItems;
    private Order order;

    @InjectMocks
    OrderItemService orderItemService;

    @BeforeEach
    void setup() {
        orderItem = new OrderItem(1L, order, new Product(), 1);

        OrderItem orderItem1 = new OrderItem(1L, order, new Product(), 1);
        OrderItem orderItem2 = new OrderItem(1L, order, new Product(), 1);
        orderItems = Arrays.asList(orderItem1, orderItem2);

        order = new Order();
        order.setOrderItems(orderItems);
    }

    @Test
    void findOrderItemsByOrderIdTest() {
        Long orderId = 1L;
        List<OrderItem> expectedOrderItems = orderItems;
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(expectedOrderItems);

        List<OrderItem> actualOrderItems = orderItemService.findOrderItemsByOrderId(orderId);

        verify(orderItemRepository).findByOrderId(orderId);
        assertEquals(actualOrderItems.size(), 2);
        assertEquals(expectedOrderItems, actualOrderItems);
    }

    @Test
    void findOrderItemsByOrderIdWhenOrderNotFoundTest() {
        Long nonExistentOrderId = 123L;
        when(orderItemRepository.findByOrderId(nonExistentOrderId)).thenReturn(Collections.emptyList());

        List<OrderItem> orderItems = orderItemService.findOrderItemsByOrderId(nonExistentOrderId);

        assertNotNull(orderItems);
        assertTrue(orderItems.isEmpty());
    }

    @Test
    void findOrderItemByIdTest() {
        Long id = 1L;
        OrderItem expectedOrderItem = orderItem;

        when(orderItemRepository.findById(id)).thenReturn(Optional.of(expectedOrderItem));

        Optional<OrderItem> actualOrderItem = orderItemService.findOrderItemById(id);

        verify(orderItemRepository).findById(id);
        assertTrue(actualOrderItem.isPresent());
        assertEquals(expectedOrderItem, actualOrderItem.get());
    }

    @Test
    void findOrderItemByIdNotFoundTest() {
        Long nonExistentId = 123L;
        when(orderItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<OrderItem> actualOrderItem = orderItemService.findOrderItemById(nonExistentId);

        verify(orderItemRepository).findById(nonExistentId);
        assertTrue(actualOrderItem.isEmpty());
    }

    @Test
    void createOrderItemsFromBasketItemsTest() {

    }

    @Test
    void createOrderItemsFromBasketItemsWhenBasketIsEmpyTest() {

    }

    @Test
    void createOrderItemsFromBasketItemsWhenProductQuantityIsUnavailableTest() {

    }

    @Test
    void returnOrderItemsToInventoryTest() {

    }

    @Test
    void isProductQuantityAvailableTest() {

    }

    @Test
    void isProductQuantityAvailableWhenNotAvailableTest() {

    }

    @Test
    void removeProductQuantityFromInventoryTest() {

    }

    @Test
    void returnProductQuantityToInventoryTest() {

    }
}