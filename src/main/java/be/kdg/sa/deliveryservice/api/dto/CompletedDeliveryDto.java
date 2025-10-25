package be.kdg.sa.deliveryservice.api.dto;

import java.util.Date;
import java.util.UUID;

public record CompletedDeliveryDto(
        UUID deliveryId,
        Date endDelivery,
        double amount
) {}
