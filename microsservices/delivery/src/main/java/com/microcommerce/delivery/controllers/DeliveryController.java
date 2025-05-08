package com.microcommerce.delivery.controllers;

import com.microcommerce.delivery.dto.CreateDeliveryDTO;
import com.microcommerce.delivery.dto.DeliveryPriceDTO;
import com.microcommerce.delivery.dto.PublicDeliveryDTO;
import com.microcommerce.delivery.exception.DeliveryAlreadyExistentException;
import com.microcommerce.delivery.services.DeliveryService;
import feign.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {
    private DeliveryService deliveryService;
    private ModelMapper modelMapper;

    public DeliveryController(DeliveryService deliveryService, ModelMapper modelMapper) {
        this.deliveryService = deliveryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<PublicDeliveryDTO> createDelivery(@RequestBody @Valid CreateDeliveryDTO createDeliveryDTO) {
        try {
            var createdDelivery = deliveryService.createDelivery(createDeliveryDTO);
            PublicDeliveryDTO publicDeliveryDTO = modelMapper.map(createdDelivery, PublicDeliveryDTO.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(publicDeliveryDTO);
        } catch (DeliveryAlreadyExistentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/price")
    public ResponseEntity<DeliveryPriceDTO> getDeliveryPrice(@RequestParam("zipCode") @NotEmpty String zipCode) {
        var price = deliveryService.getDeliveryPrice(zipCode);
        DeliveryPriceDTO deliveryPriceDTO = new DeliveryPriceDTO();
        deliveryPriceDTO.setPrice(price);

        return ResponseEntity.status(HttpStatus.OK).body(deliveryPriceDTO);
    }
}
