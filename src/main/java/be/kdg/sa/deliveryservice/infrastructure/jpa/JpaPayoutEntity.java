package be.kdg.sa.deliveryservice.infrastructure.jpa;

import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.delivery.DeliveryId;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import be.kdg.sa.deliveryservice.domain.payout.PayoutId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Table(name = "payout")
public class JpaPayoutEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID courierId;

    @Column(nullable = false)
    private UUID deliveryId;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Date payoutDate;

    protected JpaPayoutEntity() {}

    public JpaPayoutEntity(UUID id, UUID courierId, UUID deliveryId, double amount, Date payoutDate) {
        this.id = id;
        this.courierId = courierId;
        this.deliveryId = deliveryId;
        this.amount = amount;
        this.payoutDate = payoutDate;
    }

    public static JpaPayoutEntity fromDomain(Payout payout) {
        return new JpaPayoutEntity(
                payout.payoutId().id(),
                payout.courierId().id(),
                payout.deliveryId().id(),
                payout.amount(),
                payout.payoutDate()
        );
    }

    public Payout toDomain() {
        return new Payout(
                new PayoutId(id),
                new CourierId(courierId),
                new DeliveryId(deliveryId),
                amount,
                payoutDate
        );
    }
}