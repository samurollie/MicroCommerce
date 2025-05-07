package com.microcommerice.customers.service.impl;

import com.microcommerice.customers.dto.UserDto;
import com.microcommerice.customers.dto.request.ChangePasswordRequest;
import com.microcommerice.customers.dto.request.UpdateCustomerRequest;
import com.microcommerice.customers.entity.Customer;
import com.microcommerice.customers.exception.ResourceNotFoundException;
import com.microcommerice.customers.exception.UnauthorizedException;
import com.microcommerice.customers.repository.CustomerRepository;
import com.microcommerice.customers.security.services.UserDetailsImpl;
import com.microcommerice.customers.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getCurrentCustomer(Authentication authentication) {
        Customer user = getCustomerFromAuthentication(authentication);
        return mapUserToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateCustomer(Authentication authentication, UpdateCustomerRequest updateRequest) {
        Customer customer = getCustomerFromAuthentication(authentication);

        if (updateRequest.getFirstName() != null) {
            customer.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null) {
            customer.setLastName(updateRequest.getLastName());
        }

        if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().equals(customer.getEmail()) &&
                customerRepository.existsByEmail(updateRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        } else if (updateRequest.getEmail() != null) {
            customer.setEmail(updateRequest.getEmail());
        }

        Customer savedUser = customerRepository.save(customer);
        return mapUserToDto(savedUser);
    }

    @Override
    @Transactional
    public void changePassword(Authentication authentication, ChangePasswordRequest request) {
        Customer customer = getCustomerFromAuthentication(authentication);

        // Validate old password
        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Validate new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        // Update password
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
    }

    @Override
    public List<UserDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapUserToDto(customer);
    }

    @Override
    @Transactional
    public void toggleCustomerStatus(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        customer.setActive(!customer.isActive());
        customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        Long customerId;

        if (principal instanceof UserDetailsImpl) {
            customerId = ((UserDetailsImpl) principal).getId();
        } else {
            throw new UnauthorizedException("Invalid authentication principal");
        }

        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserDto mapUserToDto(Customer user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setActive(user.isActive());

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        dto.setRoles(roles);

        return dto;
    }
}