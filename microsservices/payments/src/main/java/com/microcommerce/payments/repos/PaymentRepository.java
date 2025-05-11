package com.microcommerce.payments.repos;

import com.microcommerce.payments.enums.PaymentStatus;
import com.microcommerce.payments.models.PaymentModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<PaymentModel, Long> {
    PaymentModel getPaymentModelByOrderId(String orderId);

    List<PaymentModel> findByStatus(PaymentStatus status);
}
