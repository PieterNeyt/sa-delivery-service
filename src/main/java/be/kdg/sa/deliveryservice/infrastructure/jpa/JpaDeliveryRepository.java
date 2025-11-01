package be.kdg.sa.deliveryservice.infrastructure.jpa;

import be.kdg.sa.deliveryservice.domain.delivery.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDeliveryRepository extends JpaRepository<JpaDeliveryEntity, UUID> {

    Optional<JpaDeliveryEntity> findByOrderId(UUID orderId);

    List<JpaDeliveryEntity> findByDeliveryStatus(DeliveryStatus deliveryStatus);

    List<JpaDeliveryEntity> findByCourierIdAndDeliveryStatus(UUID courierId, DeliveryStatus deliveryStatus);
}
