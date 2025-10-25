package be.kdg.sa.deliveryservice.api.dto;

import java.util.List;

public record CourierEarningsDto(
        List<CompletedDeliveryDto> deliveries,
        double total
) {}