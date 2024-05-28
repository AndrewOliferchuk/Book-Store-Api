package com.example.demo.service;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart createShoppingCart(Long userId);

    void removeBookFromCart(Long cartItemId);

    ShoppingCartResponseDto getCartByUser(Long userId);

    ShoppingCartResponseDto addBookToShopCart(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity);

}
