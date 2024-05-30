package com.example.demo.dto.shoppingcart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<Long> cartItemsId;
}
