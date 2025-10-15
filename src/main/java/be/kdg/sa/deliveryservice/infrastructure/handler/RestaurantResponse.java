package be.kdg.sa.deliveryservice.infrastructure.handler;

import java.util.UUID;

public record RestaurantResponse(UUID orderId,
                                 String isAccepted,
                                 String message) {
}
