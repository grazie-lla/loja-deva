package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/shopping-basket")
public class ShoppingBasketController {

    private final ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    public ShoppingBasketController(ShoppingBasketRepository shoppingBasketRepository) {
        this.shoppingBasketRepository = shoppingBasketRepository;
    }

    @GetMapping("/shopping-basket")
    public List<ShoppingBasket> buscarTodos(){
        return shoppingBasketRepository.findAll();
    }
}
