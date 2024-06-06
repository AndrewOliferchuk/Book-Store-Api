package com.example.demo.service;

import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto create(OrderRequestDto requestDto, User user);

    List<OrderResponseDto> findAllOrder(User user, Pageable pageable);

    List<OrderItemResponseDto> findAllOrderItems(User user, Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItem(User user, Long orderId, Long id);

    void updateStatus(Long id, OrderRequestDto requestDto);
}
