package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.dto.BasketItemRequest;
import com.tech.ada.java.lojadeva.dto.UpdateItemQuantityRequest;
import com.tech.ada.java.lojadeva.service.BasketItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BasketItemControllerTest {
    @Mock
    private BasketItemService basketItemService;

    @InjectMocks
    private BasketItemController basketItemController;

    private BasketItem basketItem1;
    private BasketItem basketItem2;
    private BasketItemRequest basketItemRequest;
    private UpdateItemQuantityRequest updateItemQuantityRequest;

    @BeforeEach
    void setUp() {
        ShoppingBasket shoppingBasket = new ShoppingBasket();

        basketItem1 = new BasketItem();
        basketItem1.setId(1L);
        basketItem1.setQuantity(2);
        basketItem1.setShoppingBasket(shoppingBasket);
        basketItem1.setProduct(new Product());

        basketItem2 = new BasketItem();
        basketItem2.setId(2L);
        basketItem2.setQuantity(3);
        basketItem2.setShoppingBasket(shoppingBasket);
        basketItem2.setProduct(new Product());

        basketItemRequest = new BasketItemRequest();
        basketItemRequest.setQuantity(4);
        basketItemRequest.setShoppingBasketId(1L);
        basketItemRequest.setProductId(1L);

        updateItemQuantityRequest = new UpdateItemQuantityRequest();
        updateItemQuantityRequest.setQuantity(5);
    }

    @Test
    void findAllItemsHttpRequest() throws Exception {
        List<BasketItem> expectedItems = Arrays.asList(basketItem1, basketItem2);
        when(basketItemService.findAllItems()).thenReturn(expectedItems);

        List<BasketItem> actualItems = basketItemController.findAllItems();
        assertEquals(expectedItems, actualItems);

        verify(basketItemService, times(1)).findAllItems();
    }

    @Test
    void findItemByIdHttpRequest() throws Exception {
        Long id = 1L;
        Optional<BasketItem> expectedItem = Optional.of(basketItem1);
        when(basketItemService.findItemById(id)).thenReturn(expectedItem);

        ResponseEntity<BasketItem> actualResponse = basketItemController.findItemById(id);

        assertEquals(ResponseEntity.ok(basketItem1), actualResponse);
        verify(basketItemService, times(1)).findItemById(id);
    }

    @Test
    void findItemByIdNotFoundHttpRequest() throws Exception {
        Long id = 3L;
        Optional<BasketItem> expectedItem = Optional.empty();
        when(basketItemService.findItemById(id)).thenReturn(expectedItem);

        ResponseEntity<BasketItem> actualResponse = basketItemController.findItemById(id);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
        verify(basketItemService, times(1)).findItemById(id);
    }

    @Test
    void findItemsByShoppingBasketIdHttpRequest() throws Exception {
        Long shoppingBasketId = 1L;
        List<BasketItem> expectedItems = Arrays.asList(basketItem1, basketItem2);
        when(basketItemService.findItemsByShoppingBasketId(shoppingBasketId)).thenReturn(expectedItems);

        List<BasketItem> actualItems = basketItemController.findItemsByShoppingBasketId(shoppingBasketId);

        assertEquals(expectedItems, actualItems);
        verify(basketItemService, times(1)).findItemsByShoppingBasketId(shoppingBasketId);
    }

    @Test
    void createItemHttpRequest() throws Exception {
        BasketItem expectedItem = new BasketItem();
        expectedItem.setId(3L);
        expectedItem.setQuantity(4);
        expectedItem.setShoppingBasket(new ShoppingBasket());
        expectedItem.setProduct(new Product());
        when(basketItemService.createItem(basketItemRequest)).thenReturn(expectedItem);

        ResponseEntity<BasketItem> actualResponse = basketItemController.createItem(basketItemRequest);

        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(expectedItem), actualResponse);
        verify(basketItemService, times(1)).createItem(basketItemRequest);
    }

    @Test
    void deleteItemHttpRequest() throws Exception {
        Long id = 1L;
        when(basketItemService.deleteItem(id)).thenReturn(true);

        ResponseEntity<String> actualResponse = basketItemController.deleteItem(id);

        assertEquals(ResponseEntity.ok("Item deletado com sucesso!"), actualResponse);
        verify(basketItemService, times(1)).deleteItem(id);
    }

    @Test
    void deleteItemNotFoundHttpRequest() throws Exception {
        Long id = 3L;
        when(basketItemService.deleteItem(id)).thenReturn(false);

        ResponseEntity<String> actualResponse = basketItemController.deleteItem(id);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
        verify(basketItemService, times(1)).deleteItem(id);
    }

    @Test
    void updateItemQuantityHttpRequest() throws Exception {
        Long id = 1L;
        BasketItem expectedItem = new BasketItem();
        expectedItem.setId(1L);
        expectedItem.setQuantity(5);
        expectedItem.setShoppingBasket(new ShoppingBasket());
        expectedItem.setProduct(new Product());
        when(basketItemService.updateItemQuantity(id, 5)).thenReturn(expectedItem);

        ResponseEntity<BasketItem> actualResponse = basketItemController.updateItemQuantity(id, updateItemQuantityRequest);

        assertEquals(ResponseEntity.ok(expectedItem), actualResponse);
        verify(basketItemService, times(1)).updateItemQuantity(id, 5);
    }

    @Test
    void updateItemQuantityNotFoundHttpRequest() throws Exception {
        Long id = 3L;
        when(basketItemService.updateItemQuantity(id, 5)).thenReturn(null);

        ResponseEntity<BasketItem> actualResponse = basketItemController.updateItemQuantity(id, updateItemQuantityRequest);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
        verify(basketItemService, times(1)).updateItemQuantity(id, 5);
    }
}