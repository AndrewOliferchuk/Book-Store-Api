package com.example.demo.mapper;

import com.example.demo.dto.order.OrderItemResponseDto;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemResponseDto toDto(OrderItem orderItem);
}
