package com.microcommerice.cart.dtos;

import lombok.Data;

@Data
public class UpdateQuantityRequest {
    private Long cartItemId;
    private Integer quantity;
}