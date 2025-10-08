package be.kdg.sa.deliveryservice.application;

import be.kdg.sa.deliveryservice.domain.Courier;
import be.kdg.sa.deliveryservice.domain.Delivery;
import be.kdg.sa.deliveryservice.domain.DeliveryRepository;
import be.kdg.sa.deliveryservice.domain.DeliveryStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, CourierRepository courierRepository) {
        this.deliveryRepository = deliveryRepository;
        this.courierRepository = courierRepository;
    }

    public List<Delivery> getAllAvailableDeliveries() {
        return deliveryRepository.findAllAvailableDeliveries();
    }

    public Delivery claimDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow();
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow();

        if(delivery.getDeliveryStatus() != DeliveryStatus.AVAILABLE)
            throw new IllegalStateException("Delivery status is not AVAILABLE");

        if(courierRepository.hasActiveDelivery(courier.getId().id()))
            throw new IllegalStateException("courier already has a active delivery");

        delivery.assignCourier(courier.getId());
        delivery.changeDeliveryStatus(DeliveryStatus.ACCEPTED);
        delivery.StartDelivery();

        deliveryRepository.save(delivery);
        return delivery;
    }
}
