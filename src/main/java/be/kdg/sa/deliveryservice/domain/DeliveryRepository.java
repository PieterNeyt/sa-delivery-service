package be.kdg.sa.deliveryservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    List<Delivery> findAllAvailableDeliveries();

    Optional<Delivery> findById(UUID id);
    Optional<Delivery> findByOrderId(UUID orderId);

    void save(Delivery delivery);
    void savePayout(Payout payout);
    List<Delivery> findCompletedDeliveriesByCourier(UUID courierId);
    List<Payout> findPayoutsByCourier(UUID courierId);

}
