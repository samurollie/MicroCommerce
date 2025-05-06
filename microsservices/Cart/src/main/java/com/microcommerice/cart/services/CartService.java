package com.microcommerice.cart.services;

import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartDto;
import com.microcommerice.cart.exceptions.ItemNotFoundException;
import com.microcommerice.cart.models.Cart;
import com.microcommerice.cart.models.CartItem;
import com.microcommerice.cart.repositories.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public CartDto getCartByUserId(String userId) {
        Cart cart = findOrCreateCartByUserId(userId);

        if (cart.getItems().isEmpty()) {
            throw new ItemNotFoundException("Cart is empty");
        }

        return mapCartToDTO(cart);
    }

    public CartDto addItemToCart(String userId, AddItemRequest itemRequest) {
        Cart cart = findOrCreateCartByUserId(userId);
        Long productId = Long.valueOf(itemRequest.getProductId());

        Optional<CartItem> existingItemOpt = findCartItemByProductId(cart, productId);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + itemRequest.getQuantity());
        } else {
            CartItem newItem = createCartItem(productId, itemRequest.getQuantity());
            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return mapCartToDTO(savedCart);
    }

    public CartDto removeItemFromCart(String userId, String productId) {
        Cart cart = findOrCreateCartByUserId(userId);
        Long productIdLong = Long.valueOf(productId);

        Optional<CartItem> itemToRemoveOpt = findCartItemByProductId(cart, productIdLong);

        if (itemToRemoveOpt.isPresent()) {
            CartItem itemToRemove = itemToRemoveOpt.get();
            cart.removeItem(itemToRemove);
            cartRepository.save(cart);
        } else {
            throw new ItemNotFoundException("Item not found in cart");
        }

        return mapCartToDTO(cart);
    }

    public CartDto updateItemQuantity(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            return removeItemFromCart(userId, productId);
        }

        Cart cart = findOrCreateCartByUserId(userId);
        Long productIdLong = Long.valueOf(productId);

        Optional<CartItem> itemToUpdateOpt = findCartItemByProductId(cart, productIdLong);

        if (itemToUpdateOpt.isPresent()) {
            CartItem itemToUpdate = itemToUpdateOpt.get();
            itemToUpdate.setQuantity(quantity);
        } else {
            CartItem newItem = createCartItem(productIdLong, quantity);
            cart.addItem(newItem);
        }

        Cart updatedCart = cartRepository.save(cart);
        return mapCartToDTO(updatedCart);
    }

    public void deleteCart(String userId) {
        cartRepository.findByUserId(Long.valueOf(userId)).ifPresent(cartRepository::delete);
    }

    private Cart findOrCreateCartByUserId(String userId) {
        Long userIdLong = Long.valueOf(userId);
        return cartRepository.findByUserId(userIdLong)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userIdLong);
                    return cartRepository.save(newCart);
                });
    }

    private Optional<CartItem> findCartItemByProductId(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private CartItem createCartItem(Long productId, int quantity) {
        CartItem newItem = new CartItem();
        newItem.setProductId(productId);
        newItem.setQuantity(quantity);
        return newItem;
    }

    private CartDto mapCartToDTO(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(String.valueOf(cart.getUserId()));
        dto.setItems(cart.getItems().stream()
                .map(this::mapCartItemToDTO)
                .collect(Collectors.toList()));
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