package be.kdg.sa.deliveryservice.application;

import be.kdg.sa.deliveryservice.domain.courier.Courier;

import java.util.Optional;
import java.util.UUID;

public interface CourierRepository {
    Optional<Courier> findById(UUID id);

    boolean hasActiveDelivery(UUID id);
}
