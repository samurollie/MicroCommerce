package com.microcommerice.cart.services;

import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartDto;
import com.microcommerice.cart.models.Cart;
import com.microcommerice.cart.models.CartItem;
import com.microcommerice.cart.repositories.CartItemRepository;
import com.microcommerice.cart.repositories.CartRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok annotation for constructor injection
@Transactional // All public methods will be transactional by default
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    // Inject WebClient or RestTemplate here if you need to call the catalogue service

    public CartDto getCartByUserId(String userId) {
        Cart cart = findOrCreateCartByUserId(userId);
        return mapCartToDTO(cart);
    }

    public CartDto addItemToCart(String userId, AddItemRequest itemRequest) {
        Cart cart = findOrCreateCartByUserId(userId);

        // Check if item already exists in cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemRequest.getProductId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Update quantity
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + itemRequest.getQuantity());
            cartItemRepository.save(existingItem); // Explicit save might be needed depending on cascading/fetch types
        } else {
            // Add new item
            // TODO: Optionally validate productId and fetch price from Catalogue Service here
            CartItem newItem = new CartItem();
            newItem.setProductId(Long.valueOf(itemRequest.getProductId()));
            newItem.setQuantity(itemRequest.getQuantity());
            // newItem.setPrice(fetchedPrice); // If fetching price
            cart.addItem(newItem); // This also sets the cart relationship
            // cartRepository.save(cart); // Saving cart cascades to new item
        }
        // Need to save the cart explicitly IF CartItem relationship is not managed by JPA correctly on add
        Cart savedCart = cartRepository.save(cart);
        return mapCartToDTO(savedCart);
    }

    public CartDto removeItemFromCart(String userId, String productId) {
        Cart cart = findOrCreateCartByUserId(userId); // Or throw exception if cart must exist

        Optional<CartItem> itemToRemoveOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (itemToRemoveOpt.isPresent()) {
            CartItem itemToRemove = itemToRemoveOpt.get();
            cart.removeItem(itemToRemove); // Removes item from list and sets item.cart to null
            // cartItemRepository.delete(itemToRemove); // Deletion happens via orphanRemoval=true or explicit call
            cartRepository.save(cart); // Save cart to persist removal
        } else {
            // Optionally throw an exception if item not found
            System.out.println("Item not found in cart: " + productId);
        }

        return mapCartToDTO(cart);
    }

    public CartDto updateItemQuantity(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            return removeItemFromCart(userId, productId);
        }

        Cart cart = findOrCreateCartByUserId(userId);

        Optional<CartItem> itemToUpdateOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (itemToUpdateOpt.isPresent()) {
            CartItem itemToUpdate = itemToUpdateOpt.get();
            itemToUpdate.setQuantity(quantity);
            cartItemRepository.save(itemToUpdate); // Save the updated item
        } else {
            // Optionally throw an exception if item not found
            System.out.println("Item not found in cart for update: " + productId);
            // Or potentially add it as a new item? Depends on desired logic.
        }

        // Fetch the potentially modified cart again before mapping
        Cart updatedCart = cartRepository.findById(cart.getId()).orElse(cart); // Re-fetch or trust the in-memory state
        return mapCartToDTO(updatedCart);
    }


    public void deleteCart(String userId) {
        cartRepository.findByUserId(Long.valueOf(userId)).ifPresent(cartRepository::delete);
    }


    private Cart findOrCreateCartByUserId(String userId) {
        return cartRepository.findByUserId(Long.valueOf(userId))
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(Long.valueOf(userId));
                    return cartRepository.save(newCart);
                });
    }

    private CartDto mapCartToDTO(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(String.valueOf(cart.getUserId()));
        dto.setItems(cart.getItems().stream()
                .map(this::mapCartItemToDTO)
                .collect(Collectors.toList()));
        // TODO: Here you could potentially enrich the DTOs
        // with data fetched from the catalogue service (product name, current price, image url etc.)
        // based on the productIds in the items.
        return dto;
    }

    private CartDto.CartItemDTO mapCartItemToDTO(CartItem item) {
        CartDto.CartItemDTO itemDto = new CartDto.CartItemDTO();
        itemDto.setId(item.getId());
        itemDto.setProductId(String.valueOf(item.getProductId()));
        itemDto.setQuantity(item.getQuantity());
        return itemDto;
    }
}