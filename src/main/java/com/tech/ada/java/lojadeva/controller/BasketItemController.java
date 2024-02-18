package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import com.tech.ada.java.lojadeva.dto.BasketItemRequest;
import com.tech.ada.java.lojadeva.dto.UpdateItemQuantityRequest;
import com.tech.ada.java.lojadeva.repository.BasketItemRepository;
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
    private final BasketItemRepository basketItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BasketItemController(BasketItemRepository basketItemRepository, ModelMapper modelMapper) {
        this.basketItemRepository = basketItemRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<BasketItem> findAllItems(){
        return basketItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public BasketItem findItemById(@PathVariable Long id){
        Optional<BasketItem> optionalBasketItem = basketItemRepository.findById(id);
        return optionalBasketItem.orElse(null);
    }

    @GetMapping(params = {"shoppingBasketId"})
    public List<BasketItem> findItemByShoppingBasketId(@RequestParam Long shoppingBasketId){
        return basketItemRepository.findByShoppingBasketId(shoppingBasketId);
    }

    @PostMapping
    public ResponseEntity<BasketItem> createItem(@RequestBody BasketItemRequest request) {
        BasketItem basketItemConvertido = modelMapper.map(request, BasketItem.class);
        BasketItem newBasketItem = basketItemRepository.save(basketItemConvertido);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBasketItem);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") Long id) {
        basketItemRepository.deleteById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BasketItem> updateItemQuantity(@PathVariable Long id, @RequestBody UpdateItemQuantityRequest request) {
        Optional<BasketItem> optionalBasketItem = basketItemRepository.findById(id);

        if(optionalBasketItem.isPresent()) {
            BasketItem basketItem = optionalBasketItem.get();
            basketItem.setQuantity(request.getQuantity());
            BasketItem savedBasketItem =  basketItemRepository.save(basketItem);
            return ResponseEntity.ok(savedBasketItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
