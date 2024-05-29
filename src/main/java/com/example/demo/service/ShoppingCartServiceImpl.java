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
import jakarta.transaction.Transactional;
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
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public void removeBookFromCart(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for id: "
                        + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getCartByUser(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Can't find shopping cart for user with id:" + userId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
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
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Shopping cart not found for user id: " + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for id: "
                        + cartItemId));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }
}
