package com.tech.ada.java.lojadeva.service.impl;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShoppingBasketServiceImplTest {

    @Mock
    private ShoppingBasketRepository shoppingBasketRepository;

    @InjectMocks
    private ShoppingBasketServiceImpl shoppingBasketService;

    private ShoppingBasket shoppingBasket;

    private Product product;

    private List<ShoppingBasket> shoppingBasketList;

    private List<BasketItem> basketItemList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        shoppingBasket = new ShoppingBasket();
        shoppingBasketList = new ArrayList<>();
        product = new Product("IPhone", "Smartphone", new BigDecimal("1000.99"), 10, "Eletrônicos");
        basketItemList = new ArrayList<>();
        BasketItem basketItem1 = new BasketItem();
        basketItem1.setId(1L);
        basketItem1.setQuantity(2);
        basketItem1.setShoppingBasket(shoppingBasket);
        basketItem1.setProduct(product);
        basketItemList.add(basketItem1);
    }

    @Test
    public void testFindAllBaskets() {
        when(shoppingBasketRepository.findAll()).thenReturn(shoppingBasketList);

        List<ShoppingBasket> result = shoppingBasketService.findAllBaskets();

        assertEquals(shoppingBasketList, result);
    }

    @Test
    public void testFindBasketById() {
        shoppingBasket.setId(1L);
        shoppingBasket.setBasketItems(basketItemList);

        when(shoppingBasketRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(shoppingBasket));

        Optional<ShoppingBasket> result = shoppingBasketService.findBasketById(1L);


        assertTrue(result.isPresent());
        assertEquals(shoppingBasket.getId(), result.get().getId());
        assertEquals(shoppingBasket.getTotal(), result.get().getTotal());
    }

    @Test
    public void testCreateShoppingBasket() {

        when(shoppingBasketRepository.save(shoppingBasket)).thenReturn(shoppingBasket);

        ShoppingBasket result = shoppingBasketService.createShoppingBasket(shoppingBasket);

        assertEquals(shoppingBasket, result);
    }
}
