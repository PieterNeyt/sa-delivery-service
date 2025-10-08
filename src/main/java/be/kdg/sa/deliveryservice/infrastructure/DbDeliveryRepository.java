package be.kdg.sa.deliveryservice.infrastructure;

import be.kdg.sa.deliveryservice.domain.Delivery;
import be.kdg.sa.deliveryservice.domain.DeliveryRepository;
import be.kdg.sa.deliveryservice.domain.DeliveryStatus;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaDeliveryEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaDeliveryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbDeliveryRepository implements DeliveryRepository {
    private final JpaDeliveryRepository jpaDeliveryRepository;

    public DbDeliveryRepository(JpaDeliveryRepository jpaDeliveryRepository) {
        this.jpaDeliveryRepository = jpaDeliveryRepository;
    }

    @Override
    public List<Delivery> findAllAvailableDeliveries() {
        return this.jpaDeliveryRepository.findAll().stream()
                .filter(d -> d.getDeliveryStatus() == DeliveryStatus.AVAILABLE)
                .map(JpaDeliveryEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Delivery> findById(UUID id) {
        return this.jpaDeliveryRepository.findById(id).map(JpaDeliveryEntity::toDomain);
    }

    @Override
    public void save(Delivery delivery) {
        this.jpaDeliveryRepository.save(JpaDeliveryEntity.fromDomain(delivery));
    }
}
