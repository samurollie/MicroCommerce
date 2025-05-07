package com.microcommerice.cart.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CartDTO {
    private UUID id;
    private String userId;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice; // Calculado com base nos preços dos produtos

    @Data
    public static class CartItemDTO {
        private UUID id; // ID do CartItem
        private String productId;
        private int quantity;
        private String productName;
        private BigDecimal productPrice; // Preço unitário do produto
        private BigDecimal itemTotalPrice; // quantity * productPrice
    }
}