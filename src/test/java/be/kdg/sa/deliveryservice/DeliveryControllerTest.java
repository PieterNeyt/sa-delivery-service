package be.kdg.sa.deliveryservice;


import be.kdg.sa.deliveryservice.api.DeliveryController;
import be.kdg.sa.deliveryservice.api.dto.CourierEarningsDto;
import be.kdg.sa.deliveryservice.api.dto.DeliveryDto;
import be.kdg.sa.deliveryservice.application.DeliveryService;
import be.kdg.sa.deliveryservice.domain.delivery.Delivery;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryControllerTest {

    private DeliveryService deliveryService;
    private DeliveryController controller;

    @BeforeEach
    void setUp() {
        deliveryService = mock(DeliveryService.class);
        controller = new DeliveryController(deliveryService);
    }


    @Test
    void getCompletedDeliveries_ReturnsDto() {
        UUID courierId = UUID.randomUUID();

        CourierEarningsDto earningsDto = mock(CourierEarningsDto.class);
        when(deliveryService.getCompletedDeliveriesAndPayments(courierId)).thenReturn(earningsDto);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(courierId.toString());

        ResponseEntity<CourierEarningsDto> response = controller.getCompletedDeliveries(jwt);

        assertEquals(earningsDto, response.getBody());
        verify(deliveryService).getCompletedDeliveriesAndPayments(courierId);
    }
}