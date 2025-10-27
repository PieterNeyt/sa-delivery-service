package be.kdg.sa.deliveryservice.domain.payout;

import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Date;

@Getter
@Entity
public class Payout {
    @Identity
    private final PayoutId payoutId;
    private final CourierId courierId;
    private final DeliveryId deliveryId;
    private final double amount;
    private final Date payoutDate;

    public Payout(CourierId courierId, DeliveryId deliveryId, double amount) {
        this.payoutId = PayoutId.create();
        this.courierId = courierId;
        this.deliveryId = deliveryId;
        this.amount = amount;
        this.payoutDate = new Date();
    }
    public Payout(PayoutId payoutId, CourierId courierId, DeliveryId deliveryId, double amount, Date payoutDate) {
        this.payoutId = payoutId;
        this.courierId = courierId;
        this.deliveryId = deliveryId;
        this.amount = amount;
        this.payoutDate = payoutDate;
    }
}