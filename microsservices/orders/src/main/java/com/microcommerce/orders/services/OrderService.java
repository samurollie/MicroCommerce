package com.microcommerce.orders.services;

import com.microcommerce.orders.dto.CreateOrderDTO;
import com.microcommerce.orders.dto.CreateOrderItemDTO;
import com.microcommerce.orders.models.OrderItemModel;
import com.microcommerce.orders.models.OrderModel;
import com.microcommerce.orders.repos.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderModel createOrder(CreateOrderDTO createOrderDTO) {
        OrderModel orderModel = new OrderModel();

        orderModel.setCustomerId(createOrderDTO.getCustomerId());

        for (CreateOrderItemDTO createOrderItemDTO : createOrderDTO.getOrderItems()) {
            OrderItemModel orderItemModel = new OrderItemModel();
            orderItemModel.setName(createOrderItemDTO.getName());
            orderItemModel.setPrice(createOrderItemDTO.getPrice());
            orderItemModel.setQuantity(createOrderItemDTO.getQuantity());
            orderItemModel.setOrder(orderModel);

            orderModel.getItems().add(orderItemModel);
        }

        return this.orderRepository.save(orderModel);
    }

    public List<OrderModel> findByUserId(Long userId) {
        return orderRepository.findByCustomerId(userId.toString());
    }
}
