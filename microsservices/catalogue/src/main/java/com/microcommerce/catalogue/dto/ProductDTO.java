package com.microcommerce.catalogue.dto;

import com.microcommerce.catalogue.models.ProductType;
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
    private String imageName;
    private String imageUrl;
}
