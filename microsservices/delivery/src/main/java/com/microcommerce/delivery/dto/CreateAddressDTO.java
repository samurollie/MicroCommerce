package com.microcommerce.delivery.dto;

import jakarta.persistence.Column;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateAddressDTO {
    @NotNull
    private String street;
    @NotNull
    private String houseNumber;
    @NotNull
    private String complement;
    @NotNull
    private String district;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String zipCode;
}

