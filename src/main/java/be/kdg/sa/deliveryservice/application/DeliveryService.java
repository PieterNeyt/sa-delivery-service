package be.kdg.sa.deliveryservice.application;

import be.kdg.sa.deliveryservice.api.dto.CompletedDeliveryDto;
import be.kdg.sa.deliveryservice.api.dto.CourierEarningsDto;
import be.kdg.sa.deliveryservice.domain.*;
import be.kdg.sa.deliveryservice.infrastructure.handler.RestaurantResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;

    @Value("${payout.standard.compensation}")
    private double basicCompensation;

    @Value("${payout.standard.incremental}")
    private double perMinuteExtra;

    public DeliveryService(DeliveryRepository deliveryRepository, CourierRepository courierRepository) {
        this.deliveryRepository = deliveryRepository;
        this.courierRepository = courierRepository;
    }

    public void processAcceptedOrder(RestaurantResponse msg) {
        Delivery delivery = new Delivery(new OrderId(msg.orderId()) );
        deliveryRepository.save(delivery);
    }

    public List<Delivery> getAllAvailableDeliveries() {
        return deliveryRepository.findAllAvailableDeliveries();
    }

    public Delivery completeDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow();

        if(delivery.getDeliveryStatus() != DeliveryStatus.IN_ROUTE)
            throw new IllegalStateException("Delivery status is not accepted");

        if(!delivery.getCourierId().id().equals(courierId))
            throw new IllegalStateException("This delivery is assigned to another courier");

        Payout payout = delivery.completeDelivery(basicCompensation, perMinuteExtra);

        deliveryRepository.save(delivery);
        deliveryRepository.savePayout(payout);

        return delivery;
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
        delivery.acceptDelivery();
        delivery.StartDelivery();

        deliveryRepository.save(delivery);
        return delivery;
    }

    public Delivery cancelClaimDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow();

        if (!delivery.getCourierId().id().equals(courierId))
            throw new IllegalStateException("Delivery doesn't belong to courier");

        delivery.cancelDelivery();

        deliveryRepository.save(delivery);
        return delivery;
    }

    public CourierEarningsDto getCompletedDeliveriesAndPayments(UUID courierId) {
        List<Delivery> deliveries = deliveryRepository.findCompletedDeliveriesByCourier(courierId);
        List<Payout> payouts = deliveryRepository.findPayoutsByCourier(courierId);


        Map<UUID, Double> payoutMap = payouts.stream()
                .collect(Collectors.toMap(p -> p.getDeliveryId().id(), Payout::getAmount));

        List<CompletedDeliveryDto> dtoList = deliveries.stream()
                .map(d -> new CompletedDeliveryDto(
                        d.getDeliveryId().id(),
                        d.getEndDelivery(),
                        payoutMap.getOrDefault(d.getDeliveryId().id(), 0.0)
                ))
                .toList();

        double total = payouts.stream().mapToDouble(Payout::getAmount).sum();

        return new CourierEarningsDto(dtoList, total);
    }

}
