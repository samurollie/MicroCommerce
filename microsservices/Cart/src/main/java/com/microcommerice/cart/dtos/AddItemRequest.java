package com.microcommerice.cart.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddItemRequest {

    private Long productId;

    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;
    private String productName;
    private BigDecimal unitPrice;
    private String imageUrl;
}
