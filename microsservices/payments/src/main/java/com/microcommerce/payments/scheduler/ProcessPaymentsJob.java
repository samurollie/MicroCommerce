package com.microcommerce.payments.scheduler;

import com.microcommerce.payments.enums.PaymentStatus;
import com.microcommerce.payments.models.PaymentModel;
import com.microcommerce.payments.repos.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class ProcessPaymentsJob {
    private static final Logger logger = LoggerFactory.getLogger(ProcessPaymentsJob.class);
    private PaymentRepository paymentRepository;
    private Random random;

    public ProcessPaymentsJob(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.random = new Random();
    }

    @Scheduled(fixedRate = 10000) // 10,000 is 10 seconds
    public void processPayments() {
        List<PaymentModel> pendingPayments = this.paymentRepository.findByStatus(PaymentStatus.PENDING);

        for (PaymentModel paymentModel : pendingPayments) {
            boolean isProcessed = this.random.nextBoolean();

            if (!isProcessed) {
                logger.info("Payment with ID {} was not processed.", paymentModel.getId());
                continue;
            }

            boolean isApproved = this.random.nextBoolean();

            if (isApproved) {
                paymentModel.setStatus(PaymentStatus.APPROVED);
                paymentModel.setApprovedAt(new java.util.Date());
                logger.info("Payment with ID {} was APPROVED.", paymentModel.getId());
            } else {
                paymentModel.setStatus(PaymentStatus.REFUSED);
                logger.info("Payment with ID {} was REFUSED.", paymentModel.getId());
            }

            this.paymentRepository.save(paymentModel);
        }
    }
}
