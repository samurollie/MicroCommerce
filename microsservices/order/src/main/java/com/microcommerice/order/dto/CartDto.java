package com.microcommerice.order.dto;

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
        private String productId;
        private int quantity;
    }
}
