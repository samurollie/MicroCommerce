package com.microcommerice.customers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String street;

    @NotBlank
    @Size(min = 1, max = 10)
    private String number;

    private String complement;

    @NotBlank
    @Size(min = 2, max = 100)
    private String neighborhood; // bairro

    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @NotBlank
    @Size(max = 2)
    private String state;

    @NotBlank
    @Size(max = 20)
    private String zipCode;

    @NotBlank
    @Size(min = 2, max = 100)
    private String country;

    private boolean main;

}
