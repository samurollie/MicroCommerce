package com.microcommerice.cart.services;

import com.microcommerice.cart.dtos.AddItemRequest;
import com.microcommerice.cart.dtos.CartResponse;
import com.microcommerice.cart.dtos.UpdateQuantityRequest;

public interface CartService {
    CartResponse getCart(String userId);
    CartResponse addItem(String userId, AddItemRequest request);
    CartResponse updateItemQuantity(String userId, UpdateQuantityRequest request);
    CartResponse removeItem(String userId, Long cartItemId);
    void clearCart(String userId);
    CartResponse checkout(String userId);
}