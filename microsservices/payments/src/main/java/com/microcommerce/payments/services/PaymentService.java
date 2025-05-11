package com.microcommerce.payments.services;

import com.microcommerce.payments.dto.CreatePaymentDTO;
import com.microcommerce.payments.enums.PaymentMethod;
import com.microcommerce.payments.enums.PaymentStatus;
import com.microcommerce.payments.exception.PaymentAlreadyExistentException;
import com.microcommerce.payments.exception.PaymentNotFoundException;
import com.microcommerce.payments.models.CreditCardInfoModel;
import com.microcommerce.payments.models.DebitCardInfoModel;
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

        if ((createPaymentDTO.getPaymentMethod() == PaymentMethod.CREDIT_CARD ||
                createPaymentDTO.getPaymentMethod() == PaymentMethod.DEBIT_CARD) &&
                createPaymentDTO.getCardInfo() == null) {
            throw new IllegalArgumentException("Card info is required for credit and debit card payments.");
        }

        PaymentModel paymentModel = new PaymentModel();

        paymentModel.setStatus(PaymentStatus.PENDING);
        paymentModel.setAmount(createPaymentDTO.getAmount());
        paymentModel.setPaymentMethod(createPaymentDTO.getPaymentMethod());
        paymentModel.setOrderId(createPaymentDTO.getOrderId());

        if (createPaymentDTO.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
            CreditCardInfoModel creditCardInfoModel = new CreditCardInfoModel();

            creditCardInfoModel.setCardNumber(createPaymentDTO.getCardInfo().getCardNumber());
            creditCardInfoModel.setCardHolderName(createPaymentDTO.getCardInfo().getCardHolderName());
            creditCardInfoModel.setExpirationDate(createPaymentDTO.getCardInfo().getExpirationDate());
            creditCardInfoModel.setCvv(createPaymentDTO.getCardInfo().getCvv());

            paymentModel.setCreditCard(creditCardInfoModel);

            System.out.println("AAAAAAAAAAAAAAAA");
        }

        if (createPaymentDTO.getPaymentMethod() == PaymentMethod.DEBIT_CARD) {
            DebitCardInfoModel debitCardInfoModel = new DebitCardInfoModel();

            debitCardInfoModel.setCardNumber(createPaymentDTO.getCardInfo().getCardNumber());
            debitCardInfoModel.setCardHolderName(createPaymentDTO.getCardInfo().getCardHolderName());
            debitCardInfoModel.setExpirationDate(createPaymentDTO.getCardInfo().getExpirationDate());
            debitCardInfoModel.setCvv(createPaymentDTO.getCardInfo().getCvv());

            paymentModel.setDebitCard(debitCardInfoModel);
        }

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
