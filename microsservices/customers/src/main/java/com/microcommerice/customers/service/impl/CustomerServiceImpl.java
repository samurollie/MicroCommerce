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


    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getCurrentUser(Authentication authentication) {
        Customer user = getUserFromAuthentication(authentication);
        return mapUserToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Authentication authentication, UpdateCustomerRequest updateRequest) {
        Customer user = getUserFromAuthentication(authentication);

        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }

        if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        } else if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }

        Customer savedUser = userRepository.save(user);
        return mapUserToDto(savedUser);
    }

    @Override
    @Transactional
    public void changePassword(Authentication authentication, ChangePasswordRequest request) {
        Customer user = getUserFromAuthentication(authentication);

        // Validate old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Validate new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        Customer user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapUserToDto(user);
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long id) {
        Customer user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    public Customer getUserFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        Long userId;

        if (principal instanceof UserDetailsImpl) {
            userId = ((UserDetailsImpl) principal).getId();
        } else {
            throw new UnauthorizedException("Invalid authentication principal");
        }

        return userRepository.findById(userId)
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