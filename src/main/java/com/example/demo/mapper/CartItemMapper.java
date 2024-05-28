package com.example.demo.mapper;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.CartItemResponseDto;
import com.example.demo.model.CartItem;

public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toModel(CartItemRequestDto cartItemRequestDto);
}
