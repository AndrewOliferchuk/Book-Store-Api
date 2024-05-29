package com.example.demo.controller;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping("/shopping_cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBookToCart(Authentication authentication,
                         @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToShopCart(user.getId(), cartItemRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get shopping cart",
            description = "Get shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCartByUser(user.getId());
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Update cart item",
            description = "Update cart item")
    public ShoppingCartResponseDto update(@PathVariable @Positive Long cartItemId,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(cartItemId,
                cartItemRequestDto.getQuantity(), user.getId());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping
    @Operation(summary = "Delete Book from cart",
            description = "Delete cartItem by Id")
    public void delete(Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.removeBookFromCart(cartItemId, user.getId());
    }
}
