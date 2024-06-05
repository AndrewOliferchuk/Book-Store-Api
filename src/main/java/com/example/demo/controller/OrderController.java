package com.example.demo.controller;

import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {
    private OrderService orderService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    @Operation(summary = "Create order",
            description = "Create order")
    public OrderResponseDto createOrder(@AuthenticationPrincipal User user,
                                        @RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.create(requestDto, user);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get all orders")
    public List<OrderResponseDto> allOrders(@AuthenticationPrincipal User user,
                                           Pageable pageable) {
        return orderService.findAllOrder(user, pageable);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all orderitems by order", description = "Get all orderitems")
    public List<OrderItemResponseDto> getOrderItems(@AuthenticationPrincipal User user,
                             @PathVariable Long orderId, Pageable pageable) {
        return orderService.findAllOrderItems(user,orderId, pageable);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get orderitems by order", description = "Get orderitems by order")
    public OrderItemResponseDto getOrderItem(@AuthenticationPrincipal User user,
                           @PathVariable Long orderId, @PathVariable Long id) {
        return orderService.getOrderItem(user, orderId, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    @Operation(summary = "Update status", description = "Update status")
    public void updateStatus(@Valid @RequestBody OrderRequestDto requestDto,
                             @PathVariable Long id) {
        orderService.updateStatus(id, requestDto);
    }
}
