package com.microcommerce.catalogue.dto;

import com.microcommerce.catalogue.models.ProductType;
import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private float price;
    private int quantity;
    private ProductType type = ProductType.OTHER;
    private Long sellerId;
}
