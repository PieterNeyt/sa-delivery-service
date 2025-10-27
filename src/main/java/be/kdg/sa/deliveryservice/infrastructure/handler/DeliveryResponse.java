package be.kdg.sa.deliveryservice.infrastructure.handler;

import java.util.UUID;

public record DeliveryResponse(UUID orderId, String status, String message) { }