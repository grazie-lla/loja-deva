package com.tech.ada.java.lojadeva.service.impl;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.domain.ShoppingBasket;
import com.tech.ada.java.lojadeva.repository.BasketItemRepository;
import com.tech.ada.java.lojadeva.service.BasketItemService;
import com.tech.ada.java.lojadeva.service.ProductService;
import com.tech.ada.java.lojadeva.service.ShoppingBasketService;
import com.tech.ada.java.lojadeva.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketItemServiceImpl implements BasketItemService {
    private final BasketItemRepository basketItemRepository;
    private final ShoppingBasketService shoppingBasketService;
    private final ProductService productService;

    @Autowired
    public BasketItemServiceImpl(BasketItemRepository basketItemRepository, ShoppingBasketService shoppingBasketService, ProductService productService) {
        this.basketItemRepository = basketItemRepository;
        this.shoppingBasketService = shoppingBasketService;
        this.productService = productService;
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
        ShoppingBasket shoppingBasket = shoppingBasketService.findBasketById(item.getShoppingBasketId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));

        Product product = productService.findProductById(item.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (product.getInventoryQuantity() == 0) {
            throw new IllegalArgumentException("Produto indisponível no estoque.");
        }

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
