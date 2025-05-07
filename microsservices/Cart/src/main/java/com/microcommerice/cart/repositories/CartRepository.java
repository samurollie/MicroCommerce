package com.microcommerice.cart.repositories;

import com.microcommerice.cart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(String userId);

    void deleteByUserId(String userId); // Para limpar o carrinho
}