package com.tech.ada.java.lojadeva.service.impl;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.BasketItemRepository;
import com.tech.ada.java.lojadeva.service.BasketItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketItemServiceImpl implements BasketItemService {
    private final BasketItemRepository basketItemRepository;

    @Autowired
    public BasketItemServiceImpl(BasketItemRepository basketItemRepository) {
        this.basketItemRepository = basketItemRepository;
    }

    @Override
    public List<BasketItem> findAllItems() {
        return basketItemRepository.findAll();
    }

    @Override
    public Optional<BasketItem> findItemById(Long id) {
        return basketItemRepository.findById(id);
    }

    @Override
    public List<BasketItem> findItemsByShoppingBasketId(Long shoppingBasketId) {
        return basketItemRepository.findByShoppingBasketId(shoppingBasketId);
    }

    @Override
    public BasketItem createItem(BasketItem item) {
        ShoppingBasket shoppingBasket;
        Product product;
        return basketItemRepository.save(item);
    }

    @Override
    public Boolean deleteItem(Long id) {
        if (basketItemRepository.findById(id).isPresent()) {
            basketItemRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BasketItem updateItemQuantity(Long id, Integer quantity) {
        Optional<BasketItem> optionalBasketItem = basketItemRepository.findById(id);
        if(optionalBasketItem.isPresent()) {
            BasketItem basketItem = optionalBasketItem.get();
            basketItem.setQuantity(quantity);
            return basketItemRepository.save(basketItem);
        }
        return null;
    }
}
