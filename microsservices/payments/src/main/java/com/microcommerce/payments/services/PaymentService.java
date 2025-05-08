package com.microcommerce.payments.services;

import com.microcommerce.payments.dto.CreatePaymentDTO;
import com.microcommerce.payments.enums.PaymentStatus;
import com.microcommerce.payments.exception.PaymentAlreadyExistentException;
import com.microcommerce.payments.exception.PaymentNotFoundException;
import com.microcommerce.payments.models.PaymentModel;
import com.microcommerce.payments.repos.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentService {
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentModel save(CreatePaymentDTO createPaymentDTO) {
        PaymentModel existentPaymentByOrderId = paymentRepository.getPaymentModelByOrderId(createPaymentDTO.getOrderId());

        if (existentPaymentByOrderId != null) {
            throw new PaymentAlreadyExistentException("Payment already existent for order id: " + createPaymentDTO.getOrderId());
        }

        PaymentModel paymentModel = new PaymentModel();

        paymentModel.setStatus(PaymentStatus.PENDING);
        paymentModel.setAmount(createPaymentDTO.getAmount());
        paymentModel.setPaymentMethod(createPaymentDTO.getPaymentMethod());
        paymentModel.setOrderId(createPaymentDTO.getOrderId());

        return paymentRepository.save(paymentModel);
    }

    public PaymentModel getPaymentModelByOrderId(String orderId) {
        PaymentModel paymentModel = paymentRepository.getPaymentModelByOrderId(orderId);

        if (paymentModel == null) {
            throw new PaymentNotFoundException("Payment not existent for order id: " + orderId);
        }

        return paymentModel;
    }
    
}
