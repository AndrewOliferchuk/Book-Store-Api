package com.example.demo.mapper;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    Order toModel(OrderRequestDto requestDto);

    OrderResponseDto toDto(Order order);
}
