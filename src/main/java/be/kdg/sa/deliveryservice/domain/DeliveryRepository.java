package be.kdg.sa.deliveryservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    List<Delivery> findAllAvailableDeliveries();

    Optional<Delivery> findById(UUID id);

    void save(Delivery delivery);
}
