package com.microcommerice.cart.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartDto {

    private UUID id;
    private String userId;
    private List<CartItemDTO> items;

    @Data
    public static class CartItemDTO {
        private UUID id;
        private String productId;
        private int quantity;
        // to add product details fetched from catalogue service
        // private String productName;
        // private BigDecimal productPrice;
    }
}
