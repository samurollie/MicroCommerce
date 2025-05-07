package com.microcommerice.cart.dtos;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
}