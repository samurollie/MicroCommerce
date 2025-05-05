package com.microcommerice.cart.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
