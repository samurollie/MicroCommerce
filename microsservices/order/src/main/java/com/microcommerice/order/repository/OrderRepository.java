package com.microcommerice.order.repository;

import com.microcommerice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndCustomerId(UUID id, String customerId);
    List<Order> findByCustomerIdOrderByOrderDateDesc(String userId);
}
