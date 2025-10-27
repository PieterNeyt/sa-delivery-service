package be.kdg.sa.deliveryservice;



import be.kdg.sa.deliveryservice.api.DeliveryController;
import be.kdg.sa.deliveryservice.api.dto.CourierDto;
import be.kdg.sa.deliveryservice.api.dto.CourierEarningsDto;
import be.kdg.sa.deliveryservice.api.dto.DeliveryDto;
import be.kdg.sa.deliveryservice.application.DeliveryService;
import be.kdg.sa.deliveryservice.domain.courier.Courier;
import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.delivery.Delivery;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryId;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryStatus;
import be.kdg.sa.deliveryservice.domain.delivery.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController controller;

    @Test
    void getAllAvailableDeliveries_ReturnsListOfDeliveries() {
        // Arrange
        Delivery delivery1 = new Delivery(
                new DeliveryId(UUID.randomUUID()),
                new OrderId(UUID.randomUUID()),
                null, null, null,
                DeliveryStatus.AVAILABLE
        );
        Delivery delivery2 = new Delivery(
                new DeliveryId(UUID.randomUUID()),
                new OrderId(UUID.randomUUID()),
                null, null, null,
                DeliveryStatus.AVAILABLE
        );

        when(deliveryService.getAllAvailableDeliveries()).thenReturn(List.of(delivery1, delivery2));

        // Act
        ResponseEntity<List<DeliveryDto>> response = controller.GetAllAvailableDeliveries();

        // Assert
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(deliveryService).getAllAvailableDeliveries();
    }

    @Test
    void claimDelivery_ReturnsClaimed() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();

        Delivery delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(UUID.randomUUID()),
                null, null, null,
                DeliveryStatus.AVAILABLE
        );

        when(deliveryService.claimDelivery(deliveryId, courierId)).thenReturn(delivery);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(courierId.toString());

        // Act
        ResponseEntity<DeliveryDto> response = controller.ClaimDelivery(deliveryId, jwt);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(deliveryId, response.getBody().id());
        verify(deliveryService).claimDelivery(deliveryId, courierId);
    }

    @Test
    void getDelivery_ReturnsDelivery() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();

        Delivery delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(UUID.randomUUID()),
                null, null, null,
                DeliveryStatus.ACCEPTED
        );

        when(deliveryService.getDelivery(deliveryId, courierId)).thenReturn(delivery);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(courierId.toString());

        // Act
        ResponseEntity<DeliveryDto> response = controller.GetDelivery(deliveryId, jwt);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(deliveryId, response.getBody().id());
        verify(deliveryService).getDelivery(deliveryId, courierId);
    }

    @Test
    void completeDelivery_ReturnsCompletedDelivery() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();

        Delivery delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(UUID.randomUUID()),
                null, null, null,
                DeliveryStatus.DELIVERD
        );

        when(deliveryService.completeDelivery(deliveryId, courierId)).thenReturn(delivery);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(courierId.toString());

        // Act
        ResponseEntity<DeliveryDto> response = controller.completeDelivery(deliveryId, jwt);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(deliveryId, response.getBody().id());
        assertEquals(DeliveryStatus.DELIVERD, response.getBody().deliveryStatus());
        verify(deliveryService).completeDelivery(deliveryId, courierId);
    }

    @Test
    void cancelDeliveryClaim_ReturnsCancelledDelivery() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();

        Delivery delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(UUID.randomUUID()),
                null, null, null,
                DeliveryStatus.AVAILABLE
        );

        when(deliveryService.cancelClaimDelivery(deliveryId, courierId)).thenReturn(delivery);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(courierId.toString());

        // Act
        ResponseEntity<DeliveryDto> response = controller.CancelDeliveryClaim(deliveryId, jwt);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(deliveryId, response.getBody().id());
        assertEquals(DeliveryStatus.AVAILABLE, response.getBody().deliveryStatus());
        verify(deliveryService).cancelClaimDelivery(deliveryId, courierId);
    }

    @Test
    void getCompletedDeliveries_ReturnsDto() {
        // Arrange
        UUID courierId = UUID.randomUUID();

        CourierEarningsDto earningsDto = mock(CourierEarningsDto.class);
        when(deliveryService.getCompletedDeliveriesAndPayments(courierId)).thenReturn(earningsDto);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(courierId.toString());

        // Act
        ResponseEntity<CourierEarningsDto> response = controller.getCompletedDeliveries(jwt);

        // Assert
        assertEquals(earningsDto, response.getBody());
        verify(deliveryService).getCompletedDeliveriesAndPayments(courierId);
    }
    @Test
    void courierDto_mapsFromDomain() {
        UUID id = UUID.randomUUID();
        Courier courier = new Courier(
                new CourierId(id),
                "John",
                "Doe",
                "john@doe.com",
                "0123456",
                "Main Street",
                "BE12"
        );

        CourierDto dto = CourierDto.from(courier);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.email()).isEqualTo("john@doe.com");
        assertThat(dto.phonenumber()).isEqualTo("0123456");
        assertThat(dto.address()).isEqualTo("Main Street");
        assertThat(dto.IBAN()).isEqualTo("BE12");
    }

    @Test
    void deliveryDto_mapsFromDomain_withoutCourier() {
        UUID deliveryId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Delivery delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(orderId),
                null,
                null,
                null,
                DeliveryStatus.AVAILABLE
        );

        DeliveryDto dto = DeliveryDto.from(delivery);

        assertThat(dto.id()).isEqualTo(deliveryId);
        assertThat(dto.orderId()).isEqualTo(orderId);
        assertThat(dto.courierId()).isNull();
        assertThat(dto.deliveryStatus()).isEqualTo(DeliveryStatus.AVAILABLE);
    }

    @Test
    void deliveryDto_mapsFromDomain_withCourier() {
        UUID deliveryId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID courierId = UUID.randomUUID();
        Date startDate = new Date();
        Date endDate = new Date();

        Delivery delivery = new Delivery(
                new DeliveryId(deliveryId),
                new OrderId(orderId),
                new CourierId(courierId),
                startDate,
                endDate,
                DeliveryStatus.DELIVERD
        );

        DeliveryDto dto = DeliveryDto.from(delivery);

        assertThat(dto.id()).isEqualTo(deliveryId);
        assertThat(dto.orderId()).isEqualTo(orderId);
        assertThat(dto.courierId()).isEqualTo(courierId);
        assertThat(dto.startDelivery()).isEqualTo(startDate);
        assertThat(dto.endDelivery()).isEqualTo(endDate);
        assertThat(dto.deliveryStatus()).isEqualTo(DeliveryStatus.DELIVERD);
    }
}