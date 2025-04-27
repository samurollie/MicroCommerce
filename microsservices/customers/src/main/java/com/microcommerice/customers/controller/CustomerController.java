package com.microcommerice.customers.controller;

import com.microcommerice.customers.dto.request.AddressRequest;
import com.microcommerice.customers.dto.response.CustomerResponse;
import com.microcommerice.customers.dto.response.MessageResponse;
import com.microcommerice.customers.entity.Address;
import com.microcommerice.customers.entity.Customer;
import com.microcommerice.customers.repository.AddressRepository;
import com.microcommerice.customers.repository.CustomerRepository;
import com.microcommerice.customers.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerController(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getCurrentCustomer() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Optional<Customer> customer = customerRepository.findById(userDetails.getId());

        if (customer.isEmpty()) {
           return ResponseEntity.badRequest().body(new MessageResponse("Customer not found"));
        }

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.get().getId());
        customerResponse.setUsername(customer.get().getUsername());
        customerResponse.setEmail(customer.get().getEmail());
        customerResponse.setName(customer.get().getName());
        customerResponse.setPhone(customer.get().getPhone());
        customerResponse.setActive(customer.get().isActive());
        customerResponse.setRoles(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        
        return ResponseEntity.ok(customerResponse);
    }

    @PostMapping("/address")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressRequest addressRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Customer> customerOpt = customerRepository.findById(userDetails.getId());
        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Customer not found"));
        }

        Customer customer = customerOpt.get();

        Address address = getAddress(addressRequest, customer);

        // If this is a main address, unset any existing main address
        if (address.isMain()) {
            Optional<Address> mainAddressOpt = addressRepository.findByCustomerAndMain(customer, true);
            mainAddressOpt.ifPresent(mainAddress -> {
                mainAddress.setMain(false);
                addressRepository.save(mainAddress);
            });
        }

        addressRepository.save(address);

        return ResponseEntity.ok(new MessageResponse("Address added successfully"));
    }

    @GetMapping("/address")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAddresses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Customer> customerOpt = customerRepository.findById(userDetails.getId());
        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Customer not found"));
        }

        List<Address> addresses = addressRepository.findByCustomer(customerOpt.get());

        return ResponseEntity.ok(addresses);
    }

    private static Address getAddress(AddressRequest addressRequest, Customer customer) {
        Address address = new Address();
        address.setStreet(addressRequest.getStreet());
        address.setNumber(addressRequest.getNumber());
        address.setComplement(addressRequest.getComplement());
        address.setNeighborhood(addressRequest.getNeighborhood());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZipCode(addressRequest.getZipCode());
        address.setCountry(addressRequest.getCountry());
        address.setMain(addressRequest.isMain());
        address.setCustomer(customer);
        return address;
    }
}
