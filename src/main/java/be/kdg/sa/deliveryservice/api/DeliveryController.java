package be.kdg.sa.deliveryservice.api;

import be.kdg.sa.deliveryservice.api.dto.CourierEarningsDto;
import be.kdg.sa.deliveryservice.api.dto.DeliveryDto;
import be.kdg.sa.deliveryservice.application.DeliveryService;
import be.kdg.sa.deliveryservice.domain.delivery.Delivery;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    private UUID getIdFromToken(@AuthenticationPrincipal Jwt token) {
        return UUID.fromString(token.getClaimAsString("sub"));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DeliveryDto>> GetAllAvailableDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllAvailableDeliveries();
        return ResponseEntity.ok(deliveries.stream().map(DeliveryDto::from).toList());
    }

    @PreAuthorize("hasAuthority('courier')")
    @PostMapping("/{id}/claim")
    public ResponseEntity<DeliveryDto> ClaimDelivery(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        Delivery delivery = deliveryService.claimDelivery(id,getIdFromToken(token));
        return ResponseEntity.ok(DeliveryDto.from(delivery));
    }
    @PreAuthorize("hasAuthority('courier')")
    @PostMapping("/{id}/getDelivery")
    public ResponseEntity<DeliveryDto> GetDelivery(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        Delivery delivery = deliveryService.getDelivery(id,getIdFromToken(token));
        return ResponseEntity.ok(DeliveryDto.from(delivery));
    }
    @PreAuthorize("hasAuthority('courier')")
    @PostMapping("/{id}/completeDelivery")
    public ResponseEntity<DeliveryDto> completeDelivery(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        Delivery delivery = deliveryService.completeDelivery(id,getIdFromToken(token));
        return ResponseEntity.ok(DeliveryDto.from(delivery));
    }
    @PreAuthorize("hasAuthority('courier')")
    @PutMapping("/{id}/claim/cancel")
    public ResponseEntity<DeliveryDto> CancelDeliveryClaim(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        Delivery delivery = deliveryService.cancelClaimDelivery(id,getIdFromToken(token));
        return ResponseEntity.ok(DeliveryDto.from(delivery));
    }
    @PreAuthorize("hasAuthority('courier')")
    @GetMapping("/completed")
    public ResponseEntity<CourierEarningsDto> getCompletedDeliveries(@AuthenticationPrincipal Jwt token) {
        UUID courierId = getIdFromToken(token);
        CourierEarningsDto dto = deliveryService.getCompletedDeliveriesAndPayments(courierId);
        return ResponseEntity.ok(dto);
    }



}
