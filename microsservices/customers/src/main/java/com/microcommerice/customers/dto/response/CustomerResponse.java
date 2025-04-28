package com.microcommerice.customers.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CustomerResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private boolean active;
    private List<String> roles;
}
