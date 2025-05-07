package com.microcommerice.cart.controllers;

import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartResponse;
import com.microcommerice.cart.dtos.UpdateQuantityRequest;
import com.microcommerice.cart.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody AddItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItem(userId, request));
    }

    @PutMapping("/items")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody UpdateQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-ID") String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> checkout(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.checkout(userId));
    }
}