package com.microcommerice.cart.services.impl;

import com.microcommerice.cart.client.CatalogueClient;
import com.microcommerice.cart.client.OrderClient;
import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartResponse;
import com.microcommerice.cart.dtos.UpdateQuantityRequest;
import com.microcommerice.cart.exceptions.CartItemNotFoundException;
import com.microcommerice.cart.exceptions.CartNotFoundException;
import com.microcommerice.cart.mapper.CartMapper;
import com.microcommerice.cart.models.Cart;
import com.microcommerice.cart.models.CartItem;
import com.microcommerice.cart.repositories.CartItemRepository;
import com.microcommerice.cart.repositories.CartRepository;
import com.microcommerice.cart.services.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CatalogueClient catalogueClient;
    private final OrderClient orderClient;



    @Override
    public CartResponse getCart(String userId) {
        Cart cart = findOrCreateCart(userId);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(String userId, AddItemRequest request) {
        Cart cart = findOrCreateCart(userId);

        // Check if the item already exists in the cart
        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElse(null);

        if (existingItem != null) {
            // Update quantity if the item already exists
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            // Create a new cart item
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setProductName(request.getProductName());
            newItem.setUnitPrice(request.getUnitPrice());
            newItem.setQuantity(request.getQuantity());
            newItem.setImageUrl(request.getImageUrl());

            cart.addItem(newItem);
        }

        cart.updateTotalPrice();
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItemQuantity(String userId, UpdateQuantityRequest request) {
        Cart cart = findOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + request.getCartItemId()));

        // Ensure the item belongs to the user's cart
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new CartItemNotFoundException("Cart item does not belong to the user's cart");
        }

        if (request.getQuantity() <= 0) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        cart.updateTotalPrice();
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(String userId, Long cartItemId) {
        Cart cart = findOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemId));

        // Ensure the item belongs to the user's cart
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new CartItemNotFoundException("Cart item does not belong to the user's cart");
        }

        cart.removeItem(item);
        cartItemRepository.delete(item);

        cart.updateTotalPrice();
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        Cart cart = findOrCreateCart(userId);

        cart.getItems().forEach(item -> item.setCart(null));
        cart.getItems().clear();
        cart.setTotalPrice(java.math.BigDecimal.ZERO);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartResponse checkout(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cannot checkout an empty cart");
        }

        // Create order request from cart
        // This would call the Order service to create an order
        // For now, we'll just simulate the checkout process

        // Clear the cart after successful checkout
        CartResponse cartResponse = cartMapper.toCartResponse(cart);
        clearCart(userId);

        return cartResponse;
    }

    private Cart findOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }
}