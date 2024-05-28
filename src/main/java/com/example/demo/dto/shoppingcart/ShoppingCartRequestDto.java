package com.example.demo.dto.shoppingcart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    private Long userId;
    private Set<Long> cartItemsId;
}
