package com.microcommerice.cart.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailsDTO {
    private String id;
    private String name;
    private BigDecimal price;
    // Outros campos relevantes do produto, como imageUrl
}