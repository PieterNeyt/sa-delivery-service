package be.kdg.sa.deliveryservice.domain.delivery;


import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Getter
@ToString
@AggregateRoot
public class Delivery {
    @Identity
    private DeliveryId deliveryId;
    private final OrderId orderId;
    private CourierId courierId;

    private Date startDelivery;
    private Date endDelivery;
    private DeliveryStatus deliveryStatus;

    public Delivery(OrderId orderId) {
        this.deliveryId = DeliveryId.create();
        this.deliveryStatus = DeliveryStatus.AVAILABLE;
        this.orderId = orderId;

    }

    public Delivery(DeliveryId deliveryId, OrderId orderId,CourierId courierId, Date startDelivery, Date endDelivery, DeliveryStatus deliveryStatus) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.courierId = courierId;
        this.startDelivery = startDelivery;
        this.endDelivery = endDelivery;
        this.deliveryStatus = deliveryStatus;
    }

    private double calculateCourierPay(double basicCompansation, double perMinuteExtra, long durationInMinutes) {
        return durationInMinutes <= 5 ? basicCompansation : basicCompansation + perMinuteExtra * durationInMinutes;
    }


    public void claimByCourier(CourierId courierId) {
        if(this.deliveryStatus != DeliveryStatus.AVAILABLE)
            throw new IllegalStateException("Delivery status is not AVAILABLE");

        this.courierId = courierId;
        this.deliveryStatus= DeliveryStatus.ACCEPTED;
    }

    public void readyForPickup() {
        this.deliveryStatus= DeliveryStatus.PENDING_PICKUP;
    }
    public void pickup() {
        if(this.deliveryStatus!= DeliveryStatus.PENDING_PICKUP)
            throw new IllegalStateException("Delivery is not pending pickup");


        this.deliveryStatus= DeliveryStatus.PICKED_UP;
        this.startDelivery = Date.from(Instant.now());
    }

    public Payout complete(double basicCompensation, double perMinuteExtra) {

        if(this.deliveryStatus!= DeliveryStatus.PICKED_UP)
            throw new IllegalStateException("Delivery status is not in route");

        this.deliveryStatus = DeliveryStatus.DELIVERD;
        this.endDelivery = Date.from(Instant.now());

        long duration = TimeUnit.MILLISECONDS.toMinutes(endDelivery.getTime() - startDelivery.getTime());
        double amount = calculateCourierPay(basicCompensation, perMinuteExtra, duration);

        return new Payout(this.courierId, this.deliveryId, amount);
    }

    public void cancel() {
        if(deliveryStatus != DeliveryStatus.ACCEPTED)
            throw new IllegalStateException("Delivery is already ready for pick, you have to finish the delivery");

        this.courierId = null;
        this.startDelivery = null;
        this.deliveryStatus= DeliveryStatus.AVAILABLE;
    }

}
