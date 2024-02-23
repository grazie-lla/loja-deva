package com.tech.ada.java.lojadeva.service.impl;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.ShoppingBasketRepository;
import com.tech.ada.java.lojadeva.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        Optional<ShoppingBasket> basket = shoppingBasketRepository.findById(id);
        if (basket.isPresent()) {
            ShoppingBasket shoppingBasket = basket.get();
            List<BasketItem> items = shoppingBasket.getBasketItems();
            BigDecimal total = new BigDecimal(0);
            for (BasketItem item: items) {
                BigDecimal value = BigDecimal.valueOf(item.getQuantity()).multiply(item.getProduct().getPrice());
                total = total.add(value);
            }
            shoppingBasket.setTotal(total);
        }
        return basket;
    }

    @Override
    public ShoppingBasket createShoppingBasket(ShoppingBasket shoppingBasket) {
        shoppingBasket.setTotal(new BigDecimal(0));
        return shoppingBasketRepository.save(shoppingBasket);
    }
}
