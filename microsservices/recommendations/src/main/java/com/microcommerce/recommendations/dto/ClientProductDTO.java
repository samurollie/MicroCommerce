package com.microcommerce.recommendations.dto;

import lombok.Data;

@Data
public class ClientProductDTO {
    private String name;
    private String description;
    private float price;
    private int quantity;
    private float rating;
    private String type;
    private String seller;
}
