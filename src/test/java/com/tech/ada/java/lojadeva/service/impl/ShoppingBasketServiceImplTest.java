package com.tech.ada.java.lojadeva.service.impl;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShoppingBasketServiceImplTest {

    @Mock
    private ShoppingBasketRepository shoppingBasketRepository;

    private ShoppingBasketServiceImpl shoppingBasketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shoppingBasketService = new ShoppingBasketServiceImpl(shoppingBasketRepository);
    }

    @Test
    public void testFindAllBaskets() {
        List<ShoppingBasket> expectedBaskets = new ArrayList<>();
        when(shoppingBasketRepository.findAll()).thenReturn(expectedBaskets);

        List<ShoppingBasket> result = shoppingBasketService.findAllBaskets();

        assertEquals(expectedBaskets, result);
    }

    @Test
    public void testFindBasketById() {
        long basketId = 1L;
        ShoppingBasket mockBasket = new ShoppingBasket();
        mockBasket.setId(basketId);
        mockBasket.setTotal(new BigDecimal(0));

        when(shoppingBasketRepository.findById(basketId)).thenReturn(Optional.of(mockBasket));

        Optional<ShoppingBasket> result = shoppingBasketService.findBasketById(basketId);

        //dando erro: java.lang.NullPointerException: Cannot invoke "java.util.List.iterator()" because "items" is null
        assertTrue(result.isPresent());
        assertEquals(mockBasket.getId(), result.get().getId());
        assertEquals(mockBasket.getTotal(), result.get().getTotal());
    }

    @Test
    public void testCreateShoppingBasket() {
        ShoppingBasket newBasket = new ShoppingBasket();

        when(shoppingBasketRepository.save(newBasket)).thenReturn(newBasket);

        ShoppingBasket result = shoppingBasketService.createShoppingBasket(newBasket);

        assertEquals(newBasket, result);
    }
}
