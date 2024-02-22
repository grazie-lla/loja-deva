package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shopping-basket")
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;

    @Autowired
    public ShoppingBasketController(ShoppingBasketService shoppingBasketService) {
        this.shoppingBasketService = shoppingBasketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingBasket> findBasketById(@PathVariable Long id){
        Optional<ShoppingBasket> basket = shoppingBasketService.findBasketById(id);
        return basket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShoppingBasket> createShoppingBasket() {
        ShoppingBasket newBasket = shoppingBasketService.createShoppingBasket(new ShoppingBasket());
        return ResponseEntity.status(HttpStatus.CREATED).body(newBasket);
    }
}
