package com.microcommerice.customers.controller;

import com.microcommerice.customers.dto.request.LoginRequest;
import com.microcommerice.customers.dto.request.SignupRequest;
import com.microcommerice.customers.dto.response.JwtResponse;
import com.microcommerice.customers.entity.Customer;
import com.microcommerice.customers.entity.Role;
import com.microcommerice.customers.repository.CustomerRepository;
import com.microcommerice.customers.repository.RoleRepository;
import com.microcommerice.customers.security.jwt.JwtUtils;
import com.microcommerice.customers.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
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

// Enables CORS for all origins and marks this class as a REST controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    CustomerRepository customerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                        CustomerRepository customerRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        ); // authenticate the user with the provided username and password

        SecurityContextHolder.getContext().setAuthentication(authentication); // set the authentication in the security context
        String jwt = jwtUtils.generateJwtToken(authentication); // generate a JWT token for the authenticated user

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        )); // return the JWT token and user details in the response
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (customerRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (customerRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

       Customer customer = new Customer(
               signupRequest.getUsername(),
               signupRequest.getEmail(),
               passwordEncoder.encode(signupRequest.getPassword()),
               signupRequest.getName()
       );

        if (signupRequest.getPhone() != null) {
            customer.setPhone(signupRequest.getPhone());
        }

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        }  else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "service":
                        Role serviceRole = roleRepository.findByName(Role.ERole.ROLE_SERVICE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(serviceRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        customer.setRoles(roles); // set the roles for the new customer
        customerRepository.save(customer); // save the new customer to the database

        return ResponseEntity.ok("User registered successfully!"); // return success message
    }
}
