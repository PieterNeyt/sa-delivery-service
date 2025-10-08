package be.kdg.sa.deliveryservice.api;

import be.kdg.sa.deliveryservice.application.DeliveryService;
import be.kdg.sa.deliveryservice.domain.Delivery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<DeliveryDto>> GetAllAvailableDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllAvailableDeliveries();
        return ResponseEntity.ok(deliveries.stream().map(DeliveryDto::from).toList());
    }


    @PostMapping("/{id}/claim/{courierId}")
    public ResponseEntity<DeliveryDto> ClaimDelivery(@PathVariable UUID id, @PathVariable UUID courierId) {
        Delivery delivery = deliveryService.claimDelivery(id,courierId);
        return ResponseEntity.ok(DeliveryDto.from(delivery));
    }
}
