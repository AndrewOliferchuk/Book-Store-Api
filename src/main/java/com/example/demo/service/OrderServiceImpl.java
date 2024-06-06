package com.example.demo.service;

import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.exeption.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderItemRepository itemRepository;
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto create(OrderRequestDto requestDto, User user) {
        Order order = orderMapper.toModel(requestDto);
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        Set<CartItem> cartItems = cartItemRepository.findCartItemByShoppingCartId(user.getId());
        order.setTotal(calculateAmount(cartItems));
        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice()
                                    .multiply(new BigDecimal(cartItem.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItem(orderItems);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> findAllOrder(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemResponseDto> findAllOrderItems(User user, Long orderId,
                                         Pageable pageable) {
        return orderItemRepository.findAllByOrderIdAndUserId(orderId, user.getId(), pageable)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(User user, Long orderId, Long id) {
        return orderItemRepository.findByIdAndUserIdAndOrderId(id, user.getId(), orderId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
    }

    @Override
    public void updateStatus(Long id, OrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Order not found id:" + id));
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
    }

    private BigDecimal calculateAmount(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getBook().getPrice().multiply(
                        new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
