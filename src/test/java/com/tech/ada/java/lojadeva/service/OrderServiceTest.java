package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.*;
import com.tech.ada.java.lojadeva.dto.OrderRequest;
import com.tech.ada.java.lojadeva.dto.UpdateOrderRequest;
import com.tech.ada.java.lojadeva.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private ShoppingBasketService shoppingBasketService;
    @Mock
    private BasketItemService basketItemService;

    private Order order;
    private OrderRequest orderRequest;
    private UpdateOrderRequest updateOrderRequest;
    private List<Order> orders;
    private List<OrderItem> orderItems;
    private ShoppingBasket basket;
    private List<BasketItem> basketItems;


    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setup() {
        Client client = new Client();
        client.setId(1L);

        BasketItem basketItem1 = new BasketItem();
        basketItem1.setId(1L);
        BasketItem basketItem2 = new BasketItem();
        basketItem2.setId(2L);
        basketItems = Arrays.asList(basketItem1, basketItem2);

        basket = new ShoppingBasket();
        basket.setId(1L);
        basket.setClient(client);
        basket.setBasketItems(basketItems);
        basket.setTotal(new BigDecimal(100));

        Order order1 = new Order();
        Order order2 = new Order();
        orders = Arrays.asList(order1,order2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItems = Arrays.asList(orderItem1, orderItem2);

        order = new Order(1L, orderItems, new BigDecimal(100), PaymentMethod.PIX);

        orderRequest = new OrderRequest(1L, "PIX");

        updateOrderRequest = new UpdateOrderRequest("ENVIADO");
    }

    @Test
    public void generateOrderTest() {
        OrderService orderService = spy(new OrderService(orderRepository, orderItemService, shoppingBasketService,
                basketItemService));
        Order expectedOrder = order;

        when(shoppingBasketService.findBasketById(orderRequest.getBasketId())).thenReturn(Optional.of(basket));
        when(orderService.isValidPaymentMethod(orderRequest.getPaymentMethod())).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);
        when(orderItemService.createOrderItemsFromBasketItems(any(Order.class), anyList())).thenReturn(orderItems);
        doNothing().when(orderService).emptyShoppingBasket(any(ShoppingBasket.class));

        Order actualOrder = orderService.generateOrder(orderRequest);

        verify(shoppingBasketService).findBasketById(orderRequest.getBasketId());
        verify(orderRepository, times(orderItems.size())).save(any(Order.class));
        verify(orderItemService).createOrderItemsFromBasketItems(any(Order.class), anyList());
        verify(orderService).emptyShoppingBasket(any(ShoppingBasket.class));

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void generateOrderWhenBasketNotFoundTest() {
        OrderService orderService = spy(new OrderService(orderRepository, orderItemService, shoppingBasketService,
                basketItemService));
        Long invalidBasketId = 123L;
        OrderRequest invalidOrderRequest = new OrderRequest(invalidBasketId, "PIX");

        when(shoppingBasketService.findBasketById(invalidOrderRequest.getBasketId()))
                .thenThrow(new IllegalArgumentException("Carrinho não encontrado."));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.generateOrder(invalidOrderRequest));

        assertEquals("Carrinho não encontrado.", exception.getMessage());
        verify(shoppingBasketService).findBasketById(invalidOrderRequest.getBasketId());
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderItemService, never()).createOrderItemsFromBasketItems(any(Order.class), anyList());
        verify(orderService, never()).emptyShoppingBasket(any(ShoppingBasket.class));
    }

    @Test
    public void generateOrderWhenInvalidPaymentMethodTest() {
        OrderService orderService = spy(new OrderService(orderRepository, orderItemService, shoppingBasketService,
                basketItemService));
        OrderRequest invalidOrderRequest = new OrderRequest(basket.getId(), "INVALID_METHOD");

        when(shoppingBasketService.findBasketById(invalidOrderRequest.getBasketId())).thenReturn(Optional.of(basket));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.generateOrder(invalidOrderRequest));

        assertEquals("Método de pagamento inválido", exception.getMessage());
        verify(shoppingBasketService).findBasketById(invalidOrderRequest.getBasketId());
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderItemService, never()).createOrderItemsFromBasketItems(any(Order.class), anyList());
        verify(orderService, never()).emptyShoppingBasket(any(ShoppingBasket.class));
    }

    @Test
    public void findAllOrdersTest() {
        List<Order> expectedOrders = orders;

        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findAllOrders();

        verify(orderRepository).findAll();
        assertEquals(expectedOrders.size(), actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void findOrderByIdTest() {
        Long id = 1L;
        Order expectedOrder = order;

        when(orderRepository.findById(id)).thenReturn(Optional.of(expectedOrder));

        Optional<Order> actualOrder = orderService.findOrderById(id);

        verify(orderRepository, times(1)).findById(id);
        assertTrue(actualOrder.isPresent());
        assertEquals(expectedOrder, actualOrder.get());
    }

    @Test
    public void findOrderByIdNotFoundTest() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Order> actualOrder = orderService.findOrderById(id);

        verify(orderRepository,times(1)).findById(id);
        assertTrue(actualOrder.isEmpty());
    }

    @Test
    public void findOrdersByClientIdTest(){
        Long clientId = 1L;
        List<Order> expectedOrders = orders;

        when(orderRepository.findOrderByClientId(clientId)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findOrdersByClientId(clientId);

        verify(orderRepository).findOrderByClientId(clientId);
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void updateOrderTest() {
        Long id = 1L;
        Order expectedUpdatedOrder = order;

        when(orderRepository.findById(id)).thenReturn(Optional.of(expectedUpdatedOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(expectedUpdatedOrder);

        ResponseEntity<Order> actualUpdatedOrder = orderService.updateOrder(id, updateOrderRequest);

        verify(orderRepository).findById(id);
        verify(orderRepository).save(any(Order.class));
        verify(orderItemService, never()).returnOrderItemsToInventory(anyList());
        assertEquals(HttpStatus.OK, actualUpdatedOrder.getStatusCode());
        assertEquals(expectedUpdatedOrder, actualUpdatedOrder.getBody());
    }

    @Test
    public void updateOrderWhenAlreadyDeliveredTest() {
        Long id = 1L;
        Order deliveredOrder = order;
        deliveredOrder.setStatus(Status.ENTREGUE);

        when(orderRepository.findById(id)).thenReturn(Optional.of(deliveredOrder));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.updateOrder(id, updateOrderRequest));

        assertEquals("Não é possível alterar o pedido.", exception.getMessage());
        verify(orderRepository).findById(id);
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderItemService, never()).returnOrderItemsToInventory(anyList());
    }

    @Test
    public void updateOrderWhenCancelledTest() {
        Long id = 1L;
        Order expectedUpdatedOrder = order;
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest("CANCELADO");

        when(orderRepository.findById(id)).thenReturn(Optional.of(expectedUpdatedOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(expectedUpdatedOrder);

        ResponseEntity<Order> actualUpdatedOrder = orderService.updateOrder(id, updateOrderRequest);

        verify(orderRepository).findById(id);
        verify(orderRepository).save(any(Order.class));
        verify(orderItemService).returnOrderItemsToInventory(actualUpdatedOrder.getBody().getOrderItems());
        assertEquals(HttpStatus.OK, actualUpdatedOrder.getStatusCode());
        assertEquals(expectedUpdatedOrder, actualUpdatedOrder.getBody());
    }

    @Test
    public void updateOrderWhenOrderNotFoundTest() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Order> result = orderService.updateOrder(id, new UpdateOrderRequest("ENVIADO"));

        verify(orderRepository).findById(id);
        verify(orderRepository, never()).save(any(Order.class));
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void deleteOrderByIdTest() {
        Long id = 1L;
        Order orderToDelete = order;

        when(orderRepository.findById(id)).thenReturn(Optional.of(orderToDelete));

        assertTrue(orderService.deleteOrderById(id));
        verify(orderRepository).delete(orderToDelete);
    }

    @Test
    public void deleteOrderByIdWhenOrderNotFoundTest() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertFalse(orderService.deleteOrderById(id));
        verify(orderRepository, never()).delete(any());
    }

    @Test
    public void isValidPaymentMethodWhenValidMethodTest() {
        boolean result = orderService.isValidPaymentMethod("PIX");

        assertTrue(result);
    }

    @Test
    public void isValidPaymentMethodCaseInsensitiveTest() {
        boolean result = orderService.isValidPaymentMethod("pix");

        assertTrue(result);
    }

    @Test
    public void isValidPaymentMethodWhenInvalidMethodTest() {
        boolean result = orderService.isValidPaymentMethod("PICPAY");

        assertFalse(result);
    }

    @Test
    public void emptyShoppingBasketTest() {
        when(basketItemService.deleteItem(anyLong())).thenReturn(true);

        orderService.emptyShoppingBasket(basket);

        verify(basketItemService, times(basketItems.size())).deleteItem(anyLong());
        assertEquals(BigDecimal.ZERO, basket.getTotal());
    }

    @Test
    public void isOrderCancelledWhenOrderCancelledTest() {
        Order cancelledOrder = order;
        cancelledOrder.setStatus(Status.CANCELADO);

        boolean result = orderService.isOrderCancelled(cancelledOrder);

        assertTrue(result);
    }

    @Test
    public void isOrderCancelledWhenOrderNotCancelledTest() {
        Order notCancelledOrder = order;

        boolean result = orderService.isOrderCancelled(notCancelledOrder);

        assertFalse(result);
    }
}