package com.example.demo.service;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;
    private CartItemRepository cartItemRepository;
    private ShoppingCartMapper shoppingCartMapper;
    private CartItemMapper cartItemMapper;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    @Override
    public ShoppingCart createShoppingCart(Long userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "User not found with id " + userId));
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public void removeBookFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ShoppingCartResponseDto getCartByUser(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                        .orElseGet(() -> createShoppingCart(userId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBookToShopCart(Long userId,
                           CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "User not found with id " + userId));
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                        .orElseThrow(() -> new EntityNotFoundException(
                        "Can't found book by id: " + cartItemRequestDto.getBookId()));
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        CartItem cartItem1 = cartItemRepository.save(cartItem);

        shoppingCart.getCartItems().add(cartItem1);
        ShoppingCart shoppingCart1 = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart1);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CartItem not found with id " + cartItemId));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }
}
