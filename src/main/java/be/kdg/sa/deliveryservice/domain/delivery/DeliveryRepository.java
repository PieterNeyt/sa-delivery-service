package be.kdg.sa.deliveryservice.domain.delivery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    List<Delivery> findAllAvailableDeliveries();

    Optional<Delivery> findById(UUID id);
    Optional<Delivery> findByOrderId(UUID orderId);

    void save(Delivery delivery);
    List<Delivery> findCompletedDeliveriesByCourier(UUID courierId);
}
