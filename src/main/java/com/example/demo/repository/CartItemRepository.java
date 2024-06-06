package com.example.demo.repository;

import com.example.demo.model.CartItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    Set<CartItem> findCartItemByShoppingCartId(Long shoppingCartId);
}
