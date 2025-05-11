package com.microcommerice.customers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDto {

    private Long id;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    private boolean isDefault;
}