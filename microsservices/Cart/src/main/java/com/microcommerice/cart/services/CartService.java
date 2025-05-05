package com.microcommerice.cart.services;

import com.microcommerice.cart.repositories.CartItemRepository;
import com.microcommerice.cart.repositories.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
}
