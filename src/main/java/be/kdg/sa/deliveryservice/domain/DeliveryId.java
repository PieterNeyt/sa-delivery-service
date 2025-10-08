package be.kdg.sa.deliveryservice.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record DeliveryId(UUID id) {
    public DeliveryId {
        Assert.notNull(id, "id cannot be null");
    }
    public static DeliveryId create() {
        return new DeliveryId(UUID.randomUUID());
    }
}
