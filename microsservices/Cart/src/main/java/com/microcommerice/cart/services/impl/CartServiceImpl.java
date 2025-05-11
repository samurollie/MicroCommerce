package com.microcommerice.cart.services.impl;

import com.microcommerice.cart.client.CatalogueClient;
import com.microcommerice.cart.client.CustomerClient;
import com.microcommerice.cart.client.OrderClient;
import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.AddressDto;
import com.microcommerice.cart.dtos.CartResponse;
import com.microcommerice.cart.dtos.UpdateQuantityRequest;
import com.microcommerice.cart.exceptions.CartItemNotFoundException;
import com.microcommerice.cart.exceptions.CartNotFoundException;
import com.microcommerice.cart.exceptions.CheckoutException;
import com.microcommerice.cart.exceptions.ServiceCommunicationException;
import com.microcommerice.cart.mapper.CartMapper;
import com.microcommerice.cart.models.Cart;
import com.microcommerice.cart.models.CartItem;
import com.microcommerice.cart.repositories.CartItemRepository;
import com.microcommerice.cart.repositories.CartRepository;
import com.microcommerice.cart.services.CartService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CatalogueClient catalogueClient;
    private final OrderClient orderClient;
    private final CustomerClient customerClient;


    @Override
    public CartResponse getCart(String userId) {
        Cart cart = findOrCreateCart(userId);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(String userId, AddItemRequest request) {
        Cart cart = findOrCreateCart(userId);

        // Get product details from catalogue service to ensure accurate data and pricing
        try {
            CatalogueClient.ProductDto product = catalogueClient.getProductById(request.getProductId());

            // Validate product exists and has stock
            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }

            if (product.getStock() < request.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock available");
            }

            // Override request data with accurate product information from catalogue
            request.setProductName(product.getName());
            request.setUnitPrice(product.getPrice());
            request.setImageUrl(product.getImageUrl());

            // Check if the item already exists in the cart
            CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                    .orElse(null);

            if (existingItem != null) {
                // Update quantity if the item already exists
                existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                existingItem.setUnitPrice(product.getPrice()); // Ensure latest price
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

        } catch (FeignException e) {
            log.error("Error fetching product from catalogue service", e);
            throw new ServiceCommunicationException("Error communicating with catalogue service", e);
        }
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

    private Cart findOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public CartResponse checkout(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        String token = getToken(cart);

        // Get user's default shipping address
        List<AddressDto> addresses = customerClient.getUserAddresses("Bearer " + token);
        AddressDto shippingAddress = addresses.stream()
                .filter(AddressDto::isDefault)
                .findFirst()
                .orElseThrow(() -> new CheckoutException("No default shipping address found"));

        // Create order request from cart and shipping address
        // ...rest of checkout code...

        CartResponse cartResponse = cartMapper.toCartResponse(cart);
        clearCart(userId);

        return cartResponse;

    }

    private static String getToken(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cannot checkout an empty cart");
        }

        // Get the authentication token from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = null;
        if (authentication != null && authentication.getCredentials() instanceof String) {
            token = (String) authentication.getCredentials();
        }

        if (token == null) {
            throw new SecurityException("Authentication token not available");
        }
        return token;
    }
}