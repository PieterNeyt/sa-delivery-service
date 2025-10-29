package be.kdg.sa.deliveryservice.application;

import be.kdg.sa.deliveryservice.api.dto.CompletedDeliveryDto;
import be.kdg.sa.deliveryservice.api.dto.CourierEarningsDto;
import be.kdg.sa.deliveryservice.domain.ActionNotPossibleException;
import be.kdg.sa.deliveryservice.domain.IPdfGenartor;
import be.kdg.sa.deliveryservice.domain.NotFoundException;
import be.kdg.sa.deliveryservice.domain.courier.Courier;
import be.kdg.sa.deliveryservice.domain.courier.CourierRepository;
import be.kdg.sa.deliveryservice.domain.delivery.*;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import be.kdg.sa.deliveryservice.domain.payout.PayoutRepository;
import be.kdg.sa.deliveryservice.infrastructure.handler.DeliveryMessagePublisher;
import be.kdg.sa.deliveryservice.infrastructure.handler.DeliveryResponse;
import be.kdg.sa.deliveryservice.infrastructure.handler.RestaurantResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;
    private final PayoutRepository payoutRepository;
    private final IPdfGenartor pdfGenartor;

    private final IDeliveryMessagePublisher deliveryPublisher;

    @Value("${payout.standard.compensation}")
    private double basicCompensation;

    @Value("${payout.standard.incremental}")
    private double perMinuteExtra;

    public DeliveryService(DeliveryRepository deliveryRepository, CourierRepository courierRepository, PayoutRepository payoutRepository, IPdfGenartor pdfGenartor, DeliveryMessagePublisher deliveryPublisher) {
        this.deliveryRepository = deliveryRepository;
        this.courierRepository = courierRepository;
        this.payoutRepository = payoutRepository;
        this.pdfGenartor = pdfGenartor;
        this.deliveryPublisher = deliveryPublisher;
    }

    public void processAcceptedOrder(RestaurantResponse msg) {
        Delivery delivery = new Delivery(new OrderId(msg.orderId()));
        deliveryRepository.save(delivery);
    }

    public void processReadyOrder(RestaurantResponse msg) {
        Delivery delivery = deliveryRepository.findByOrderId(msg.orderId())
                .orElseThrow(() -> new NotFoundException("Delivery not found for order: " + msg.orderId()));

        delivery.readyForPickup();
        deliveryRepository.save(delivery);
    }

    public List<Delivery> getAllAvailableDeliveries() {
        return deliveryRepository.findAllAvailableDeliveries();
    }

    public Delivery completeDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found for courier: "+courierId));

        if (!delivery.getCourierId().id().equals(courierId))
            throw new ActionNotPossibleException("This delivery is assigned to another courier");

        Payout payout = delivery.complete(basicCompensation, perMinuteExtra,courierId);

        deliveryRepository.save(delivery);
        payoutRepository.save(payout);

        deliveryPublisher.sendDeliveredResponse(
                new DeliveryResponse(
                        delivery.getOrderId().id(),
                        delivery.getDeliveryStatus().toString(),
                        "Order is bezorgd")
        );
        return delivery;
    }

    public Delivery claimDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new NotFoundException("Delivery not found for courier: "+courierId));

        if (courierRepository.hasActiveDelivery(courierId))
            throw new ActionNotPossibleException("courier already has a active delivery");

        delivery.claimByCourier(courier.getId());

        deliveryRepository.save(delivery);
        deliveryPublisher.sendClaimedResponse(
                new DeliveryResponse(delivery.getOrderId().id(), delivery.getDeliveryStatus().toString(), "Een koerier heeft je order opgenomen")
        );

        return delivery;
    }

    public Delivery pickupDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));


        delivery.pickup(courierId);
        deliveryRepository.save(delivery);

        deliveryPublisher.sendPickedUpResponse(
                new DeliveryResponse(delivery.getOrderId().id(), delivery.getDeliveryStatus().toString(), "Order is opgepikt door courier")
        );
        return delivery;
    }

    public Delivery cancelClaimDelivery(UUID id, UUID courierId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));

        delivery.cancel(courierId);

        deliveryRepository.save(delivery);
        return delivery;
    }

    public CourierEarningsDto getCompletedDeliveriesAndPayments(UUID courierId) {
        List<Delivery> deliveries = deliveryRepository.findCompletedDeliveriesByCourier(courierId);
        List<Payout> payouts = payoutRepository.findByCourierId(courierId);


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

    public byte[] GetPayoutOverview(Date startDate,Date endDate) {
        List<Payout> payouts = payoutRepository.findAllPayoutsInBetween(startDate,endDate);
        return pdfGenartor.generatorPayoutsOverview(payouts);
    }
}
