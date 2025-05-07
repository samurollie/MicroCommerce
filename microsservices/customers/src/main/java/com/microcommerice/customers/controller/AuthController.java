package com.microcommerice.customers.controller;

import com.microcommerice.customers.dto.request.LoginRequest;
import com.microcommerice.customers.dto.request.SignupRequest;
import com.microcommerice.customers.dto.response.JwtResponse;
import com.microcommerice.customers.dto.response.MessageResponse;
import com.microcommerice.customers.entity.Customer;
import com.microcommerice.customers.entity.Role;
import com.microcommerice.customers.repository.CustomerRepository;
import com.microcommerice.customers.repository.RoleRepository;
import com.microcommerice.customers.security.jwt.JwtUtils;
import com.microcommerice.customers.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Enables CORS for all origins and marks this class as a REST controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (customerRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (customerRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new customer's account
        Customer customer = new Customer();
        customer.setUsername(signUpRequest.getUsername());
        customer.setEmail(signUpRequest.getEmail());
        customer.setPassword(encoder.encode(signUpRequest.getPassword()));
        customer.setFirstName(signUpRequest.getFirstName());
        customer.setLastName(signUpRequest.getLastName());

        Set<Role> roles = new HashSet<>();

        // By default, assign ROLE_USER
        Role customerRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(customerRole);

        // Check if admin role is requested and add it if so
        if (signUpRequest.getRoles() != null) {
            signUpRequest.getRoles().forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }
            });
        }

        customer.setRoles(roles);
        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}