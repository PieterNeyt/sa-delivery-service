package be.kdg.sa.deliveryservice.domain.delivery;


import lombok.Getter;

@Getter
public enum DeliveryStatus {
    AVAILABLE,
    ACCEPTED,
    PENDING_PICKUP,
    PICKED_UP,
    DELIVERD,
    CANCELLED

}