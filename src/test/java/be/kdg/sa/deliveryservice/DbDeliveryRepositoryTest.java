package be.kdg.sa.deliveryservice;

import be.kdg.sa.deliveryservice.api.dto.CourierDto;
import be.kdg.sa.deliveryservice.api.dto.DeliveryDto;
import be.kdg.sa.deliveryservice.domain.courier.Courier;
import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.delivery.Delivery;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryId;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryStatus;
import be.kdg.sa.deliveryservice.domain.delivery.OrderId;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import be.kdg.sa.deliveryservice.infrastructure.DbDeliveryRepository;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaDeliveryEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaDeliveryRepository;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaPayoutEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaPayoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbDeliveryRepositoryTest {

    @Mock
    private JpaDeliveryRepository jpaDeliveryRepository;

    @Mock
    private JpaPayoutRepository jpaPayoutRepository;

    @InjectMocks
    private DbDeliveryRepository dbDeliveryRepository;

    private Delivery delivery;
    private UUID deliveryId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        deliveryId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(orderId),
                null,
                null,
                null,
                DeliveryStatus.AVAILABLE
        );
    }

    @Test
    void findAllAvailableDeliveries_returnsOnlyAvailable() {
        JpaDeliveryEntity entity1 = JpaDeliveryEntity.fromDomain(delivery);
        JpaDeliveryEntity entity2 = new JpaDeliveryEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                DeliveryStatus.DELIVERD,
                null,
                null
        );

        when(jpaDeliveryRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<Delivery> result = dbDeliveryRepository.findAllAvailableDeliveries();

        assertThat(result)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(delivery);
    }

    @Test
    void savePayout_callsJpaRepository() {
        Payout payout = new Payout(new CourierId(UUID.randomUUID()), new DeliveryId(UUID.randomUUID()), 10.0);

        dbDeliveryRepository.savePayout(payout);

        verify(jpaPayoutRepository, times(1)).save(any(JpaPayoutEntity.class));
    }

    @Test
    void findById_returnsDelivery() {
        JpaDeliveryEntity entity = JpaDeliveryEntity.fromDomain(delivery);
        when(jpaDeliveryRepository.findById(deliveryId)).thenReturn(Optional.of(entity));

        Optional<Delivery> result = dbDeliveryRepository.findById(deliveryId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualToComparingFieldByFieldRecursively(delivery);
    }

    @Test
    void findByOrderId_returnsDelivery() {
        UUID orderId = UUID.randomUUID();
        UUID deliveryId = UUID.randomUUID();

        JpaDeliveryEntity entity = new JpaDeliveryEntity(
                deliveryId,
                orderId,
                null,
                DeliveryStatus.AVAILABLE,
                null,
                null
        );

        when(jpaDeliveryRepository.findAll()).thenReturn(List.of(entity));

        Optional<Delivery> result = dbDeliveryRepository.findByOrderId(orderId);

        assertThat(result).isPresent();
        assertThat(result.get().getOrderId().id()).isEqualTo(orderId);
    }

    @Test
    void findByOrderId_returnsEmptyWhenNotFound() {
        UUID orderId = UUID.randomUUID();
        when(jpaDeliveryRepository.findAll()).thenReturn(List.of());

        Optional<Delivery> result = dbDeliveryRepository.findByOrderId(orderId);

        assertThat(result).isEmpty();
    }

    @Test
    void save_callsJpaRepository() {
        Delivery delivery = new Delivery(
                new DeliveryId(UUID.randomUUID()),
                new OrderId(UUID.randomUUID()),
                null,
                null,
                null,
                DeliveryStatus.AVAILABLE
        );

        dbDeliveryRepository.save(delivery);

        verify(jpaDeliveryRepository, times(1)).save(any(JpaDeliveryEntity.class));
    }

    @Test
    void findCompletedDeliveriesByCourier_filtersCorrectly() {
        UUID courierId = UUID.randomUUID();
        UUID otherCourierId = UUID.randomUUID();

        JpaDeliveryEntity completed1 = new JpaDeliveryEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                courierId,
                DeliveryStatus.DELIVERD,
                new Date(),
                new Date()
        );

        JpaDeliveryEntity completed2 = new JpaDeliveryEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                courierId,
                DeliveryStatus.DELIVERD,
                new Date(),
                new Date()
        );

        JpaDeliveryEntity pending = new JpaDeliveryEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                courierId,
                DeliveryStatus.ACCEPTED,
                new Date(),
                null
        );

        JpaDeliveryEntity otherCourier = new JpaDeliveryEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                otherCourierId,
                DeliveryStatus.DELIVERD,
                new Date(),
                new Date()
        );

        when(jpaDeliveryRepository.findAll())
                .thenReturn(List.of(completed1, completed2, pending, otherCourier));

        List<Delivery> result = dbDeliveryRepository.findCompletedDeliveriesByCourier(courierId);

        assertThat(result).hasSize(2);
    }
}