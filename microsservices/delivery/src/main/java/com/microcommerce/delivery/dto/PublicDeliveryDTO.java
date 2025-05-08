package com.microcommerce.delivery.dto;

import com.microcommerce.delivery.enums.DeliveryStatus;
import lombok.Data;

@Data
public class PublicDeliveryDTO {
    private Long id;
    private String orderId;
    private DeliveryStatus status;
    // TODO: Add address PublicDTO
}
