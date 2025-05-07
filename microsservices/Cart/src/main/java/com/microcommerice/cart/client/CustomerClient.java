package com.microcommerice.cart.client;

import com.microcommerice.cart.dtos.AddressDto;
import com.microcommerice.cart.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "customers-service", url = "${services.customers.url:http://localhost:8086}")
public interface CustomerClient {

    @GetMapping("/api/auth/users/me")
    UserDto getCurrentUser(@RequestHeader("Authorization") String authToken);

    @GetMapping("/api/auth/addresses")
    List<AddressDto> getUserAddresses(@RequestHeader("Authorization") String authToken);
}