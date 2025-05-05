package com.microcommerice.cart.controllers;

import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartDto;
import com.microcommerice.cart.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    // Helper method to extract user ID (subject) from JWT
    private String getUserId(Jwt jwt) {
        // Adjust "sub" if your customer service uses a different claim for the user ID
        return jwt.getSubject();
    }

    @GetMapping
    public ResponseEntity<CartDto> getMyCart(@AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        CartDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody AddItemRequest addItemRequest) {
        String userId = getUserId(jwt);
        CartDto updatedCart = cartService.addItemToCart(userId, addItemRequest);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String productId,
            @RequestParam int quantity) { // Or receive quantity in request body
        String userId = getUserId(jwt);
        CartDto updatedCart = cartService.updateItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDto> removeItem(@AuthenticationPrincipal Jwt jwt, @PathVariable String productId) {
        String userId = getUserId(jwt);
        CartDto updatedCart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMyCart(@AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        cartService.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }

}
