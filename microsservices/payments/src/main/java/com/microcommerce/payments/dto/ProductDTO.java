package com.microcommerce.payments.dto;

import com.microcommerce.payments.models.ProductType;
import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private float price;
    private int quantity;
    private float rating;
    private ProductType type = ProductType.OTHER;
    private String seller;
}
