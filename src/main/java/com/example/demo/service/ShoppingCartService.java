package com.example.demo.service;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(User user);

    void removeBookFromCart(Long cartItemId, Long userId);

    ShoppingCartResponseDto getCartByUser(Long userId);

    ShoppingCartResponseDto addBookToShopCart(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity, Long userId);
}
