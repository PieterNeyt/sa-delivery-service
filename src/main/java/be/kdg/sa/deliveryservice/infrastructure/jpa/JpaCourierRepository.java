package be.kdg.sa.deliveryservice.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface JpaCourierRepository extends JpaRepository<JpaCourierEntity, UUID> {
    @Query("""
    SELECT COUNT(d) > 0
    FROM JpaDeliveryEntity d
    WHERE d.courierId = :id
      AND d.deliveryStatus NOT IN (be.kdg.sa.deliveryservice.domain.delivery.DeliveryStatus.DELIVERD,
                                   be.kdg.sa.deliveryservice.domain.delivery.DeliveryStatus.CANCELLED)
""")
    boolean hasActiveDelivery(UUID id);
}
