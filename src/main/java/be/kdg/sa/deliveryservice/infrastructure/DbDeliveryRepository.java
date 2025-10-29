package be.kdg.sa.deliveryservice.infrastructure;

import be.kdg.sa.deliveryservice.domain.delivery.Delivery;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryRepository;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryStatus;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaDeliveryEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaDeliveryRepository;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaPayoutEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaPayoutRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbDeliveryRepository implements DeliveryRepository {
    private final JpaDeliveryRepository jpaDeliveryRepository;

    public DbDeliveryRepository(JpaDeliveryRepository jpaDeliveryRepository, JpaPayoutRepository jpaPayoutRepository) {
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
    public Optional<Delivery> findByOrderId(UUID orderId) {
        return this.jpaDeliveryRepository.findAll().stream()
                .filter(d -> d.getOrderId().equals(orderId))
                .findFirst()
                .map(JpaDeliveryEntity::toDomain);
    }


    @Override
    public void save(Delivery delivery) {
        this.jpaDeliveryRepository.save(JpaDeliveryEntity.fromDomain(delivery));
    }



    @Override
    public List<Delivery> findCompletedDeliveriesByCourier(UUID courierId) {
        return jpaDeliveryRepository.findAll().stream()
                .filter(d -> d.getDeliveryStatus() == DeliveryStatus.DELIVERD)
                .filter(d -> d.getCourierId() != null && d.getCourierId().equals(courierId))
                .map(JpaDeliveryEntity::toDomain)
                .toList();
    }



}
