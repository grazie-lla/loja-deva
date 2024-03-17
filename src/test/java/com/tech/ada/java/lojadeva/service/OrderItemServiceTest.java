package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.*;
import com.tech.ada.java.lojadeva.dto.UpdateProductDetailsRequest;
import com.tech.ada.java.lojadeva.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
    private List<BasketItem> basketItems;
    private Order order;
    private Product product;

    @InjectMocks
    OrderItemService orderItemService;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setInventoryQuantity(10);

        BasketItem basketItem1 = new BasketItem();
        basketItem1.setProduct(product);
        basketItem1.setQuantity(1);

        BasketItem basketItem2 = new BasketItem();
        basketItem2.setProduct(product);
        basketItem2.setQuantity(1);

        basketItems = Arrays.asList(basketItem1, basketItem2);

        ShoppingBasket basket = new ShoppingBasket();
        basket.setBasketItems(basketItems);

        order = new Order();
        order.setId(1L);

        orderItem = new OrderItem(1L, order, product, 1);

        OrderItem orderItem2 = new OrderItem(2L, order, product, 1);

        orderItems = Arrays.asList(orderItem, orderItem2);

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
        when(orderItemRepository.findByOrderId(anyLong())).thenReturn(orderItems);

        List<OrderItem> result = orderItemService.createOrderItemsFromBasketItems(order, basketItems);

        verify(orderItemRepository, times(orderItems.size())).save(any(OrderItem.class));
        assertEquals(orderItems, result);
    }

    @Test
    void createOrderItemsFromBasketItemsWhenBasketIsEmpyTest() {
        basketItems = Collections.emptyList();
        Order orderFromEmptyBasket = new Order();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderItemService.createOrderItemsFromBasketItems(orderFromEmptyBasket, basketItems);
        });

        assertEquals("O carrinho está vazio.", exception.getMessage());
    }

    @Test
    void createOrderItemsFromBasketItemsWhenProductQuantityIsUnavailableTest() {
        BasketItem basketItem = new BasketItem();
        basketItem.setProduct(product);
        basketItem.setQuantity(20);

        List<BasketItem> mockedBasketItems = Collections.singletonList(basketItem);
        Order mockedOrder = new Order();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderItemService.createOrderItemsFromBasketItems(mockedOrder, mockedBasketItems);
        });

        assertEquals("A quantidade desejada não está disponível no estoque.", exception.getMessage());
    }

    @Test
    void returnOrderItemsToInventoryTest() {
        OrderItemService orderItemService = Mockito.spy(new OrderItemService(orderItemRepository, productService));

        orderItemService.returnOrderItemsToInventory(orderItems);

        verify(orderItemService, times(orderItems.size())).returnProductQuantityToInventory(any(Product.class), anyInt());
    }

    @Test
    void isProductQuantityAvailableTest() {
        boolean result = orderItemService.isProductQuantityAvailable(product, 1);

        assertTrue(result);
    }

    @Test
    void isProductQuantityAvailableWhenNotAvailableTest() {
        boolean result = orderItemService.isProductQuantityAvailable(product, 20);

        assertFalse(result);
    }

    @Test
    void removeProductQuantityFromInventoryTest() {
        Integer quantityToRemove = 1;
        Integer expectedInventoryQuantityAfter = product.getInventoryQuantity() - quantityToRemove;

        UpdateProductDetailsRequest expectedRequest =
                new UpdateProductDetailsRequest(null, null, expectedInventoryQuantityAfter);

        orderItemService.removeProductQuantityFromInventory(product, quantityToRemove);

        verify(productService, times(1)).updateProductDetails(product.getId(), expectedRequest);
    }

    @Test
    void returnProductQuantityToInventoryTest() {
        Integer quantityToReturn = 1;
        Integer expectedInventoryQuantityAfter = product.getInventoryQuantity() + quantityToReturn;

        UpdateProductDetailsRequest expectedRequest =
                new UpdateProductDetailsRequest(null, null, expectedInventoryQuantityAfter);

        orderItemService.returnProductQuantityToInventory(product, quantityToReturn);

        verify(productService, times(1)).updateProductDetails(product.getId(), expectedRequest);
    }
}