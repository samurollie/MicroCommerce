package com.microcommerice.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailsDto {
    private String id;
    private String name;
    private BigDecimal price;
}
