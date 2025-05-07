package com.microcommerice.customers.controller;

import com.microcommerice.customers.dto.UserDto;
import com.microcommerice.customers.dto.request.ChangePasswordRequest;
import com.microcommerice.customers.dto.request.UpdateCustomerRequest;
import com.microcommerice.customers.dto.response.MessageResponse;

import com.microcommerice.customers.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(customerService.getCurrentCustomer(authentication));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateCustomerRequest updateRequest) {
        return ResponseEntity.ok(customerService.updateCustomer(authentication, updateRequest));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        customerService.changePassword(authentication, request);
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        customerService.toggleCustomerStatus(id);
        return ResponseEntity.ok(new MessageResponse("User status updated successfully"));
    }
}