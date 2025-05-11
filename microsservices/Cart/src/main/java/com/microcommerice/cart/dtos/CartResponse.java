package com.microcommerice.cart.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private String userId;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private int totalItems;
}
