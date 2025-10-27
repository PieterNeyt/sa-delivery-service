package be.kdg.sa.deliveryservice;


import be.kdg.sa.deliveryservice.api.dto.CompletedDeliveryDto;
import be.kdg.sa.deliveryservice.api.dto.CourierEarningsDto;
import be.kdg.sa.deliveryservice.application.CourierRepository;
import be.kdg.sa.deliveryservice.application.DeliveryService;
import be.kdg.sa.deliveryservice.domain.courier.Courier;
import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.delivery.*;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import be.kdg.sa.deliveryservice.infrastructure.handler.DeliveryMessagePublisher;
import be.kdg.sa.deliveryservice.infrastructure.handler.DeliveryResponse;
import be.kdg.sa.deliveryservice.infrastructure.handler.RestaurantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private CourierRepository courierRepository;
    @Mock
    private DeliveryMessagePublisher deliveryPublisher;

    @InjectMocks
    private DeliveryService deliveryService;

    private UUID courierUuid;
    private Courier courier;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        courierUuid = UUID.randomUUID();
        courier = new Courier(new CourierId(courierUuid),
                "John", "Doe", "john@doe.com", "0123456",
                "Main Street", "BE12");

        delivery = new Delivery(new DeliveryId(UUID.randomUUID()),
                new OrderId(UUID.randomUUID()), null, null, null,
                DeliveryStatus.AVAILABLE);


        deliveryService = new DeliveryService(deliveryRepository, courierRepository, deliveryPublisher);

        TestUtils.setField(deliveryService, "basicCompensation", 5.0);
        TestUtils.setField(deliveryService, "perMinuteExtra", 0.1);
    }

    @Test
    void processAcceptedOrder_savesNewDelivery() {
        RestaurantResponse msg = new RestaurantResponse(UUID.randomUUID(), "accepted", "ok");

        deliveryService.processAcceptedOrder(msg);

        verify(deliveryRepository).save(any(Delivery.class));
    }

    @Test
    void processReadyOrder_updatesExistingDelivery() {
        RestaurantResponse msg = new RestaurantResponse(delivery.getOrderId().id(), "ready", "ok");
        when(deliveryRepository.findByOrderId(msg.orderId())).thenReturn(Optional.of(delivery));

        deliveryService.processReadyOrder(msg);

        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.PENDING_PICKUP);
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void processReadyOrder_throwsIfNotFound() {
        RestaurantResponse msg = new RestaurantResponse(UUID.randomUUID(), "ready", "ok");
        when(deliveryRepository.findByOrderId(msg.orderId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryService.processReadyOrder(msg))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Delivery not found");
    }

    @Test
    void claimDelivery_assignsCourierAndPublishes() {
        when(deliveryRepository.findById(any())).thenReturn(Optional.of(delivery));
        when(courierRepository.findById(any())).thenReturn(Optional.of(courier));
        when(courierRepository.hasActiveDelivery(any())).thenReturn(false);

        Delivery result = deliveryService.claimDelivery(UUID.randomUUID(), courierUuid);

        assertThat(result.getDeliveryStatus()).isEqualTo(DeliveryStatus.ACCEPTED);
        verify(deliveryPublisher).sendClaimedResponse(any(DeliveryResponse.class));
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void claimDelivery_throwsIfNotAvailable() {
        delivery.acceptDelivery(); // status != AVAILABLE
        when(deliveryRepository.findById(any())).thenReturn(Optional.of(delivery));
        when(courierRepository.findById(any())).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> deliveryService.claimDelivery(UUID.randomUUID(), courierUuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Delivery status is not AVAILABLE");
    }

    @Test
    void completeDelivery_savesAndPublishes() {
        delivery.assignCourier(new CourierId(courierUuid));
        delivery.acceptDelivery();
        delivery.pickup();
        when(deliveryRepository.findById(delivery.getDeliveryId().id()))
                .thenReturn(Optional.of(delivery));

        Delivery result = deliveryService.completeDelivery(delivery.getDeliveryId().id(), courierUuid);

        assertThat(result.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERD);
        verify(deliveryRepository).savePayout(any(Payout.class));
        verify(deliveryPublisher).sendDeliveredResponse(any(DeliveryResponse.class));
    }

    @Test
    void completeDelivery_throwsIfOtherCourier() {
        delivery.assignCourier(new CourierId(UUID.randomUUID()));
        delivery.acceptDelivery();
        when(deliveryRepository.findById(any())).thenReturn(Optional.of(delivery));

        assertThatThrownBy(() ->
                deliveryService.completeDelivery(delivery.getDeliveryId().id(), courierUuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("assigned to another courier");
    }

    @Test
    void cancelClaimDelivery_resetsDelivery() {
        delivery.assignCourier(new CourierId(courierUuid));
        delivery.acceptDelivery();
        when(deliveryRepository.findById(delivery.getDeliveryId().id()))
                .thenReturn(Optional.of(delivery));

        Delivery result = deliveryService.cancelClaimDelivery(delivery.getDeliveryId().id(), courierUuid);

        assertThat(result.getDeliveryStatus()).isEqualTo(DeliveryStatus.AVAILABLE);
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void getCompletedDeliveriesAndPayments_returnsCorrectDto() {
        UUID deliveryId = UUID.randomUUID();
        Delivery d = new Delivery(new DeliveryId(deliveryId),
                new OrderId(UUID.randomUUID()),
                new CourierId(courierUuid),
                new Date(), new Date(),
                DeliveryStatus.DELIVERD);

        Payout payout = new Payout(new CourierId(courierUuid), new DeliveryId(deliveryId), 10.0);

        when(deliveryRepository.findCompletedDeliveriesByCourier(courierUuid))
                .thenReturn(List.of(d));
        when(deliveryRepository.findPayoutsByCourier(courierUuid))
                .thenReturn(List.of(payout));

        CourierEarningsDto result = deliveryService.getCompletedDeliveriesAndPayments(courierUuid);

        assertThat(result.total()).isEqualTo(10.0);
        assertThat(result.deliveries()).hasSize(1);
        CompletedDeliveryDto dto = result.deliveries().get(0);
        assertThat(dto.deliveryId()).isEqualTo(deliveryId);
    }
}