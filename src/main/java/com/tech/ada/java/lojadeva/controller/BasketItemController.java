package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.dto.BasketItemRequest;
import com.tech.ada.java.lojadeva.dto.UpdateItemQuantityRequest;
import com.tech.ada.java.lojadeva.repository.BasketItemRepository;
import com.tech.ada.java.lojadeva.service.BasketItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/basket-item")
public class BasketItemController {
    private final BasketItemService basketItemService;
    private final ModelMapper modelMapper;

    @Autowired
    public BasketItemController(BasketItemService basketItemService, ModelMapper modelMapper) {
        this.basketItemService = basketItemService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/items")
    public List<BasketItem> findAllItems(){
        return basketItemService.findAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BasketItem> findItemById(@PathVariable Long id){
        Optional<BasketItem> basketItem = basketItemService.findItemById(id);
        return basketItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/items/by-basket", params = {"shoppingBasketId"})
    public List<BasketItem> findItemsByShoppingBasketId(@RequestParam Long shoppingBasketId){
        return basketItemService.findItemsByShoppingBasketId(shoppingBasketId);
    }

    @PostMapping
    public ResponseEntity<BasketItem> createItem(@RequestBody BasketItemRequest request) {
        BasketItem basketItem = modelMapper.map(request, BasketItem.class);
        BasketItem newBasketItem = basketItemService.createItem(basketItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBasketItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Long id) {
        Boolean isDeleted = basketItemService.deleteItem(id);
        if (isDeleted) {
            return ResponseEntity.ok("Item deletado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BasketItem> updateItemQuantity(@PathVariable Long id, @RequestBody UpdateItemQuantityRequest request) {
        BasketItem savedBasketItem = basketItemService.updateItemQuantity(id, request.getQuantity());
        if(savedBasketItem != null) {
            return ResponseEntity.ok(savedBasketItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
