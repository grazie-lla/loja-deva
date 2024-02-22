package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.ShoppingBasket;

import java.util.List;
import java.util.Optional;

public interface ShoppingBasketService {
    List<ShoppingBasket> findAllBaskets();

    Optional<ShoppingBasket> findBasketById(Long id);

    ShoppingBasket createShoppingBasket(ShoppingBasket shoppingBasket);
}
