package be.kdg.sa.deliveryservice.domain;


import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

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


    private double calculateCourierPay(double basicCompansation, double perMinuteExtra){

        this.endDelivery = Date.from(Instant.now());

        long deliveryTime = endDelivery.getTime() - startDelivery.getTime();
        long deliveryTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(deliveryTime);

        if(deliveryTimeInMinutes <= 5)
            return basicCompansation;

        return basicCompansation + (perMinuteExtra * deliveryTimeInMinutes);
    }

    public void assignCourier(CourierId courierId) {
        this.courierId = courierId;
    }


    public void acceptDelivery() {
        this.deliveryStatus= DeliveryStatus.ACCEPTED;
    }

    public void StartDelivery() {
        this.deliveryStatus= DeliveryStatus.IN_ROUTE;
        this.startDelivery = Date.from(Instant.now());
    }

    public Payout completeDelivery(double basicCompensation, double perMinuteExtra) {
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.endDelivery = Date.from(Instant.now());
        double amount = calculateCourierPay(basicCompensation, perMinuteExtra);

        return new Payout(this.courierId, this.deliveryId, amount);
    }

    public void cancelDelivery() {
        if(deliveryStatus != DeliveryStatus.ACCEPTED)
            throw new IllegalStateException("Delivery is already ready for pick, you have to finish the delivery");

        this.courierId = null;
        this.startDelivery = null;
        this.deliveryStatus= DeliveryStatus.AVAILABLE;
    }

}
