package be.kdg.sa.deliveryservice.api.dto;

import be.kdg.sa.deliveryservice.domain.Delivery;
import be.kdg.sa.deliveryservice.domain.DeliveryStatus;

import java.util.Date;
import java.util.UUID;

public record DeliveryDto(UUID id, UUID orderId,
                          UUID courierId,
                          Date startDelivery, Date endDelivery,
                          DeliveryStatus deliveryStatus) {

    public static DeliveryDto from(final Delivery delivery) {
        UUID courier = delivery.getCourierId() == null ?
                null:delivery.getCourierId().id();
        return new DeliveryDto(delivery.getDeliveryId().id(),
                delivery.getOrderId().id(),
                courier,
                delivery.getStartDelivery(),
                delivery.getEndDelivery(),
                delivery.getDeliveryStatus()
        );
    }
}

