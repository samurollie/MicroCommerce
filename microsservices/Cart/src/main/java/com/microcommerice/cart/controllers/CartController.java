package com.microcommerice.cart.controllers;

import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartResponse;
import com.microcommerice.cart.dtos.UpdateQuantityRequest;
import com.microcommerice.cart.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody AddItemRequest request) {
        String userId = getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItem(userId, request));
    }

    @PutMapping("/items")
    public ResponseEntity<CartResponse> updateItemQuantity(@RequestBody UpdateQuantityRequest request) {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long itemId) {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        String userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> checkout() {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.checkout(userId));
    }

    /**
     * Extract the user ID from the JWT token in the security context
     * @return the user ID from the JWT token
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new SecurityException("No authentication found");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject(); // Assuming the subject is the user ID
        }

        return authentication.getName(); // Fallback to authentication name
    }
}