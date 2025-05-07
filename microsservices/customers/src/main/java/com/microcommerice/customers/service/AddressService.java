package com.microcommerice.customers.service;

import com.microcommerice.customers.dto.AddressDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AddressService {

    List<AddressDto> getUserAddresses(Authentication authentication);

    AddressDto addAddress(Authentication authentication, AddressDto addressDto);

    AddressDto updateAddress(Authentication authentication, Long addressId, AddressDto addressDto);

    void deleteAddress(Authentication authentication, Long addressId);

    void setDefaultAddress(Authentication authentication, Long addressId);
}