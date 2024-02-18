package com.tech.ada.java.lojadeva.service.impl;

import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import com.tech.ada.java.lojadeva.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingBasketServiceImpl implements ShoppingBasketService {
    private final ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    public ShoppingBasketServiceImpl(ShoppingBasketRepository shoppingBasketRepository) {
        this.shoppingBasketRepository = shoppingBasketRepository;
    }

    @Override
    public List<ShoppingBasket> findAllBaskets() {
        return shoppingBasketRepository.findAll();
    }

    @Override
    public Optional<ShoppingBasket> findBasketById(Long id) {
        return shoppingBasketRepository.findById(id);
    }
}
