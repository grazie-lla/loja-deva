package com.tech.ada.java.lojadeva.repository;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    List<BasketItem> findByShoppingBasketId(Long shoppingBasketId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BASKET_ITEM WHERE id = :id", nativeQuery = true)
    void deleteBasketItemById(@Param("id") Long id);

    /*@Transactional
    @Modifying
    @Query(value = "DELETE FROM BASKET_ITEM WHERE basket_id = :basketId", nativeQuery = true)
    void deleteBasketItems(@Param("basket_id") Long basketId);*/

}
