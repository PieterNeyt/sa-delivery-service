package be.kdg.sa.deliveryservice.domain.payout;

import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryId;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Date;

@Entity
public record Payout(@Identity PayoutId payoutId, CourierId courierId, DeliveryId deliveryId, double amount,
                     Date payoutDate) {
    public Payout(CourierId courierId, DeliveryId deliveryId, double amount) {
        this(PayoutId.create(), courierId, deliveryId, amount, new Date());
    }
}