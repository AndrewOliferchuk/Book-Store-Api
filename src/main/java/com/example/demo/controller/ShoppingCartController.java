package com.example.demo.controller;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping_cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBookToCart(@PathVariable @Valid Long userId,
             @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addBookToShopCart(userId, cartItemRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get shopping cart",
            description = "Get shopping cart")
    public ShoppingCartResponseDto getShoppingCart(@PathVariable @Valid Long userId) {
        return shoppingCartService.getCartByUser(userId);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Update cart item",
            description = "Update cart item")
    public ShoppingCartResponseDto update(@PathVariable Long cartItemId, int quantity) {
        return shoppingCartService.updateCartItem(cartItemId, quantity);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping
    @Operation(summary = "Delete Book from cart",
            description = "Delete cartItem by Id")
    public void delete(@PathVariable Long cartItemId) {
        shoppingCartService.removeBookFromCart(cartItemId);
    }
}
