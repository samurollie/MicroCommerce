package com.microcommerce.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PublicAddressDTO {
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
