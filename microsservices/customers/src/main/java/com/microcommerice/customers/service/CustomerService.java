package com.microcommerice.customers.service;

import com.microcommerice.customers.dto.UserDto;
import com.microcommerice.customers.dto.request.ChangePasswordRequest;
import com.microcommerice.customers.dto.request.UpdateCustomerRequest;
import com.microcommerice.customers.entity.Customer;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface CustomerService {
    UserDto getCurrentUser(Authentication authentication);

    UserDto updateUser(Authentication authentication, UpdateCustomerRequest updateRequest);

    void changePassword(Authentication authentication, ChangePasswordRequest request);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void toggleUserStatus(Long id);

    Customer getUserFromAuthentication(Authentication authentication);
}
