package com.microcommerice.customers.service;

import com.microcommerice.customers.dto.UserDto;
import com.microcommerice.customers.dto.request.ChangePasswordRequest;
import com.microcommerice.customers.dto.request.UpdateCustomerRequest;
import com.microcommerice.customers.entity.Customer;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface CustomerService {
    UserDto getCurrentCustomer(Authentication authentication);

    UserDto updateCustomer(Authentication authentication, UpdateCustomerRequest updateRequest);

    void changePassword(Authentication authentication, ChangePasswordRequest request);

    List<UserDto> getAllCustomers();

    UserDto getCustomerById(Long id);

    void toggleCustomerStatus(Long id);

    Customer getCustomerFromAuthentication(Authentication authentication);
}
