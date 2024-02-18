package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.dto.BasketItemRequest;
import com.tech.ada.java.lojadeva.dto.UpdateItemQuantityRequest;

import java.util.List;
import java.util.Optional;

public interface BasketItemService {

    List<BasketItem> findAllItems();

    Optional<BasketItem> findItemById(Long id);

    List<BasketItem> findItemsByShoppingBasketId(Long shoppingBasketId);

    BasketItem createItem(BasketItem item);

    Boolean deleteItem(Long id);

    BasketItem updateItemQuantity(Long id, Integer quantity);
}
