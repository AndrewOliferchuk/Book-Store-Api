package com.example.demo.dto.order;

import com.example.demo.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank
    private String shoppingAddress;
    private Status status;
}
