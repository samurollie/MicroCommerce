package com.microcommerce.delivery.repos;

import com.microcommerce.delivery.models.DeliveryModel;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRepository extends CrudRepository<DeliveryModel, Long> {
    DeliveryModel getDeliveryModelByOrderId(String orderId);
}
