package com.microcommerice.customers.service.impl;


import com.microcommerice.customers.dto.AddressDto;
import com.microcommerice.customers.entity.Address;
import com.microcommerice.customers.entity.Customer;
import com.microcommerice.customers.exception.ResourceNotFoundException;
import com.microcommerice.customers.repository.AddressRepository;
import com.microcommerice.customers.service.AddressService;
import com.microcommerice.customers.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService userService;

    @Override
    public List<AddressDto> getUserAddresses(Authentication authentication) {
        Customer user = userService.getCustomerFromAuthentication(authentication);
        return addressRepository.findByCustomer(user).stream()
                .map(this::mapAddressToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto addAddress(Authentication authentication, AddressDto addressDto) {
        Customer user = userService.getCustomerFromAuthentication(authentication);

        Address address = new Address();
        address.setCustomer(user);
        address.setStreetAddress(addressDto.getStreetAddress());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setDefaultAddress(addressDto.isDefault());

        // If this address is set as default, unset any existing default address
        if (addressDto.isDefault()) {
            resetDefaultAddressAddress(user);
        }

        // If this is the first address for the user, make it default
        if (addressRepository.findByCustomer(user).isEmpty()) {
            address.setDefaultAddress(true);
        }

        Address savedAddress = addressRepository.save(address);
        return mapAddressToDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Authentication authentication, Long addressId, AddressDto addressDto) {
        Customer user = userService.getCustomerFromAuthentication(authentication);

        Address address = addressRepository.findByIdAndCustomer(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        address.setStreetAddress(addressDto.getStreetAddress());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());

        // Handle default address change
        if (addressDto.isDefault() && !address.isDefaultAddress()) {
            resetDefaultAddressAddress(user);
            address.setDefaultAddress(true);
        }

        Address updatedAddress = addressRepository.save(address);
        return mapAddressToDto(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Authentication authentication, Long addressId) {
        Customer user = userService.getCustomerFromAuthentication(authentication);

        Address address = addressRepository.findByIdAndCustomer(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        addressRepository.delete(address);

        // If deleted address was default and user has other addresses, make another one default
        if (address.isDefaultAddress()) {
            List<Address> remainingAddresses = addressRepository.findByCustomer(user);
            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.getFirst();
                newDefault.setDefaultAddress(true);
                addressRepository.save(newDefault);
            }
        }
    }

    @Override
    @Transactional
    public void setDefaultAddress(Authentication authentication, Long addressId) {
        Customer customer = userService.getCustomerFromAuthentication(authentication);

        Address address = addressRepository.findByIdAndCustomer(addressId, customer)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        resetDefaultAddressAddress(customer);
        address.setDefaultAddress(true);
        addressRepository.save(address);
    }

    private void resetDefaultAddressAddress(Customer user) {
        Optional<Address> currentDefault = addressRepository.findByCustomerAndDefaultAddress(user, true);
        if (currentDefault.isPresent()) {
            Address defaultAddress = currentDefault.get();
            defaultAddress.setDefaultAddress(false);
            addressRepository.save(defaultAddress);
        }
    }

    private AddressDto mapAddressToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreetAddress(address.getStreetAddress());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setDefault(address.isDefaultAddress());
        return dto;
    }
}
