package com.microcommerce.payments.controllers;

import com.microcommerce.payments.dto.CreatePaymentDTO;
import com.microcommerce.payments.dto.PublicPaymentDTO;
import com.microcommerce.payments.exception.PaymentAlreadyExistentException;
import com.microcommerce.payments.exception.PaymentNotFoundException;
import com.microcommerce.payments.models.PaymentModel;
import com.microcommerce.payments.services.PaymentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    private PaymentService paymentService;
    private ModelMapper modelMapper;

    public PaymentController(PaymentService paymentService, ModelMapper modelMapper) {
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<PublicPaymentDTO> createPayment(@RequestBody @Valid CreatePaymentDTO createPaymentDTO) {
        try {
            var createdPayment = paymentService.save(createPaymentDTO);
            PublicPaymentDTO publicPaymentDTO = modelMapper.map(createdPayment, PublicPaymentDTO.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(publicPaymentDTO);
        } catch (PaymentAlreadyExistentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<PublicPaymentDTO> getPaymentByOrderId(@RequestParam("orderId") String orderId) {
        try {
            var paymentModel = paymentService.getPaymentModelByOrderId(orderId);
            PublicPaymentDTO publicPaymentDTO = modelMapper.map(paymentModel, PublicPaymentDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(publicPaymentDTO);
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
