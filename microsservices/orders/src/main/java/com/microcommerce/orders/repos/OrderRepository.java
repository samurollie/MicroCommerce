package com.microcommerce.orders.repos;

import com.microcommerce.orders.models.OrderModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderModel, Long> {
    List<OrderModel> findByCustomerId(String string);
}
