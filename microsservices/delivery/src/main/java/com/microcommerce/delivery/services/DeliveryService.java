package com.microcommerce.delivery.services;

import com.microcommerce.delivery.dto.CreateAddressDTO;
import com.microcommerce.delivery.dto.CreateDeliveryDTO;
import com.microcommerce.delivery.enums.DeliveryStatus;
import com.microcommerce.delivery.exception.DeliveryAlreadyExistentException;
import com.microcommerce.delivery.models.AddressModel;
import com.microcommerce.delivery.models.DeliveryModel;
import com.microcommerce.delivery.repos.DeliveryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeliveryService {
    private DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public DeliveryModel createDelivery(CreateDeliveryDTO createDeliveryDTO) {
        DeliveryModel existentDeliveryByOrderId = this.deliveryRepository.getDeliveryModelByOrderId(createDeliveryDTO.getOrderId());

        if (existentDeliveryByOrderId != null) {
            throw new DeliveryAlreadyExistentException("A delivery already exists for order ID: " + createDeliveryDTO.getOrderId());
        }
        
        DeliveryModel deliveryModel = new DeliveryModel();
        deliveryModel.setOrderId(createDeliveryDTO.getOrderId());
        deliveryModel.setStatus(DeliveryStatus.PENDING);

        AddressModel addressModel = mapToAddressModel(createDeliveryDTO.getAddress());

        deliveryModel.setAddress(addressModel);

        return deliveryRepository.save(deliveryModel);
    }

    private static AddressModel mapToAddressModel(@NotNull CreateAddressDTO createAddressDTO) {
        AddressModel addressModel = new AddressModel();
        addressModel.setStreet(createAddressDTO.getStreet());
        addressModel.setCity(createAddressDTO.getCity());
        addressModel.setState(createAddressDTO.getState());
        addressModel.setDistrict(createAddressDTO.getDistrict());
        addressModel.setZipCode(createAddressDTO.getZipCode());
        addressModel.setComplement(createAddressDTO.getComplement());
        addressModel.setHouseNumber(createAddressDTO.getHouseNumber());
        return addressModel;
    }
}
