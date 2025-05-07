package com.microcommerice.customers.controller;

import com.microcommerice.customers.dto.AddressDto;
import com.microcommerice.customers.dto.response.MessageResponse;
import com.microcommerice.customers.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getUserAddresses(Authentication authentication) {
        return ResponseEntity.ok(addressService.getUserAddresses(authentication));
    }

    @PostMapping
    public ResponseEntity<AddressDto> addAddress(
            Authentication authentication,
            @Valid @RequestBody AddressDto addressDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.addAddress(authentication, addressDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(authentication, id, addressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(Authentication authentication, @PathVariable Long id) {
        addressService.deleteAddress(authentication, id);
        return ResponseEntity.ok(new MessageResponse("Address deleted successfully"));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<?> setDefaultAddress(Authentication authentication, @PathVariable Long id) {
        addressService.setDefaultAddress(authentication, id);
        return ResponseEntity.ok(new MessageResponse("Default address updated successfully"));
    }
}