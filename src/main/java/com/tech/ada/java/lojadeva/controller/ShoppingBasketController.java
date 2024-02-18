package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import com.tech.ada.java.lojadeva.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/shopping-basket")
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;
    private final ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    public ShoppingBasketController(ShoppingBasketService shoppingBasketService, ShoppingBasketRepository shoppingBasketRepository) {
        this.shoppingBasketService = shoppingBasketService;
        this.shoppingBasketRepository = shoppingBasketRepository;
    }

    @GetMapping("/shopping-basket")
    public List<ShoppingBasket> findAllBaskets(){
        return shoppingBasketService.findAllBaskets();
    }

    @PostMapping
    public ResponseEntity<ShoppingBasket> createShoppingBasket() {
        ShoppingBasket newBasket = shoppingBasketRepository.save(new ShoppingBasket());
        return ResponseEntity.status(HttpStatus.CREATED).body(newBasket);
    }
}
