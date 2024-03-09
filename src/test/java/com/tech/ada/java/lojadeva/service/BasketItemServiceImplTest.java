package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Client;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.dto.BasketItemRequest;
import com.tech.ada.java.lojadeva.repository.BasketItemRepository;
import com.tech.ada.java.lojadeva.service.exceptions.ResourceNotFoundException;
import com.tech.ada.java.lojadeva.service.impl.BasketItemServiceImpl;
import com.tech.ada.java.lojadeva.service.impl.ShoppingBasketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketItemServiceImplTest {

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private ShoppingBasketServiceImpl shoppingBasketService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BasketItemServiceImpl basketItemService;

    private BasketItem basketItem1;
    private BasketItem basketItem2;
    private BasketItemRequest basketItemRequest;
    private ShoppingBasket shoppingBasket;
    private Product product;

    @BeforeEach
    void setUp() {
        shoppingBasket = new ShoppingBasket();

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
        basketItemRequest.setShoppingBasketId(2L);
        basketItemRequest.setProductId(3L);

        shoppingBasket = new ShoppingBasket();
        shoppingBasket.setId(2L);
        shoppingBasket.setClient(new Client());

        product = new Product();
        product.setId(3L);
        product.setName("Product 3");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setInventoryQuantity(10);
    }

    @Test
    void findAllItems() {
        List<BasketItem> expectedItems = Arrays.asList(basketItem1, basketItem2);
        when(basketItemRepository.findAll()).thenReturn(expectedItems);

        List<BasketItem> actualItems = basketItemService.findAllItems();

        assertEquals(expectedItems, actualItems);
        verify(basketItemRepository, times(1)).findAll();
    }

    @Test
    void findItemById() {
        Long id = 1L;
        Optional<BasketItem> expectedItem = Optional.of(basketItem1);
        when(basketItemRepository.findById(id)).thenReturn(expectedItem);

        Optional<BasketItem> actualItem = basketItemService.findItemById(id);

        assertEquals(expectedItem, actualItem);
        verify(basketItemRepository, times(1)).findById(id);
    }

    @Test
    void findItemByIdNotFound() {
        Long id = 3L;
        Optional<BasketItem> expectedItem = Optional.empty();
        when(basketItemRepository.findById(id)).thenReturn(expectedItem);

        Optional<BasketItem> actualItem = basketItemService.findItemById(id);

        assertEquals(expectedItem, actualItem);
        verify(basketItemRepository, times(1)).findById(id);
    }

    @Test
    void findItemsByShoppingBasketId() {
        Long shoppingBasketId = 1L;
        List<BasketItem> expectedItems = Arrays.asList(basketItem1, basketItem2);
        when(basketItemRepository.findByShoppingBasketId(shoppingBasketId)).thenReturn(expectedItems);

        List<BasketItem> actualItems = basketItemService.findItemsByShoppingBasketId(shoppingBasketId);

        assertEquals(expectedItems, actualItems);
        verify(basketItemRepository, times(1)).findByShoppingBasketId(shoppingBasketId);
    }

    @Test
    void createItemSuccess() {
        BasketItem expectedItem = new BasketItem();
        expectedItem.setId(3L);
        expectedItem.setQuantity(4);
        expectedItem.setShoppingBasket(new ShoppingBasket());
        expectedItem.setProduct(new Product());
        when(shoppingBasketService.findBasketById(2L)).thenReturn(Optional.of(shoppingBasket));
        when(productService.findProductById(3L)).thenReturn(Optional.of(product));
        when(basketItemRepository.save(any(BasketItem.class))).thenReturn(expectedItem);

        BasketItem actualItem = basketItemService.createItem(basketItemRequest);

        assertEquals(expectedItem, actualItem);
        verify(shoppingBasketService, times(1)).findBasketById(2L);
        verify(productService, times(1)).findProductById(3L);
        verify(basketItemRepository, times(1)).save(any(BasketItem.class));
    }

    @Test
    void createItemThrowsExceptionWhenShoppingBasketNotFound() {
        when(shoppingBasketService.findBasketById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> basketItemService.createItem(basketItemRequest));
        verify(shoppingBasketService, times(1)).findBasketById(2L);
        verify(productService, never()).findProductById(anyLong());
        verify(basketItemRepository, never()).save(any(BasketItem.class));
    }

    @Test
    void createItemThrowsExceptionWhenProductNotFound() {
        when(shoppingBasketService.findBasketById(2L)).thenReturn(Optional.of(shoppingBasket));
        when(productService.findProductById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> basketItemService.createItem(basketItemRequest));
        verify(shoppingBasketService, times(1)).findBasketById(2L);
        verify(productService, times(1)).findProductById(3L);
        verify(basketItemRepository, never()).save(any(BasketItem.class));
    }

    @Test
    void createItemThrowsExceptionWhenProductOutOfStock() {
        product.setInventoryQuantity(0);
        when(shoppingBasketService.findBasketById(2L)).thenReturn(Optional.of(shoppingBasket));
        when(productService.findProductById(3L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> basketItemService.createItem(basketItemRequest));
        verify(shoppingBasketService, times(1)).findBasketById(2L);
        verify(productService, times(1)).findProductById(3L);
        verify(basketItemRepository, never()).save(any(BasketItem.class));
    }

    @Test
    void createItemThrowsExceptionWhenProductQuantityExceedsStock() {
        basketItemRequest.setQuantity(11);
        when(shoppingBasketService.findBasketById(2L)).thenReturn(Optional.of(shoppingBasket));
        when(productService.findProductById(3L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> basketItemService.createItem(basketItemRequest));
        verify(shoppingBasketService, times(1)).findBasketById(2L);
        verify(productService, times(1)).findProductById(3L);
        verify(basketItemRepository, never()).save(any(BasketItem.class));
    }

    @Test
    void deleteItemReturnsTrueWhenSucess() {
        Long id = 1L;
        when(basketItemRepository.findById(id)).thenReturn(Optional.of(basketItem1));

        Boolean result = basketItemService.deleteItem(id);

        assertTrue(result);
        verify(basketItemRepository, times(1)).findById(id);
        verify(basketItemRepository, times(1)).deleteBasketItemById(id);
    }

    @Test
    void deleteItemReturnsFalseWhenItemNotFound() {
        Long id = 3L;
        when(basketItemRepository.findById(id)).thenReturn(Optional.empty());

        Boolean result = basketItemService.deleteItem(id);

        assertFalse(result);
        verify(basketItemRepository, times(1)).findById(id);
        verify(basketItemRepository, never()).deleteBasketItemById(anyLong());
    }

    @Test
    void updateItemQuantitySuccess() {
        Long id = 1L;
        Integer quantity = 5;
        BasketItem expectedItem = new BasketItem();
        expectedItem.setId(1L);
        expectedItem.setQuantity(5);
        expectedItem.setShoppingBasket(shoppingBasket);
        expectedItem.setProduct(product);
        when(basketItemRepository.findById(id)).thenReturn(Optional.of(basketItem1));
        when(basketItemRepository.save(any(BasketItem.class))).thenReturn(expectedItem);

        BasketItem actualItem = basketItemService.updateItemQuantity(id, quantity);

        assertEquals(expectedItem, actualItem);
        verify(basketItemRepository, times(1)).findById(id);
        verify(basketItemRepository, times(1)).save(any(BasketItem.class));
    }

    @Test
    void updateItemQuantityReturnsNullWhenItemNotFound() {
        Long id = 5L;
        Integer quantity = 10;
        when(basketItemRepository.findById(id)).thenReturn(Optional.empty());

        BasketItem result = basketItemService.updateItemQuantity(id, quantity);

        assertNull(result);
        verify(basketItemRepository, times(1)).findById(5L);
        verify(basketItemRepository, never()).save(Mockito.any());
    }
}