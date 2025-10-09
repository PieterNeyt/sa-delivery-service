package be.kdg.sa.deliveryservice.domain;


import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Getter
@ToString
@AggregateRoot
public class Delivery {
    private DeliveryId deliveryId;
    private OrderId orderId;
    private CourierId courierId;

    private Date startDelivery;
    private Date endDelivery;
    private DeliveryStatus deliveryStatus;

    public Delivery() {
        this.deliveryId = DeliveryId.create();
        this.deliveryStatus = DeliveryStatus.AVAILABLE;
    }

    public Delivery(DeliveryId deliveryId, OrderId orderId,CourierId courierId, Date startDelivery, Date endDelivery, DeliveryStatus deliveryStatus) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.courierId = courierId;
        this.startDelivery = startDelivery;
        this.endDelivery = endDelivery;
        this.deliveryStatus = deliveryStatus;
    }


    public double calculateCourierPay() {
        double basicCompansation = 3.50;
        double perMinuteExtra = 0.30;
        this.endDelivery = Date.from(Instant.now());

        //calculate length of delivery in minutes
        long deliveryTime = startDelivery.getTime() - endDelivery.getTime();
        long deliveryTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(deliveryTime);

        //check if Delivery is more then 5 minutes else return basicCompansation
        if(deliveryTimeInMinutes <= 5)
            return basicCompansation;

        //calculate total cost (3.50 + (0.50 * total minuten of delivery)
        return basicCompansation + (perMinuteExtra * deliveryTimeInMinutes);
    }

    public void assignCourier(CourierId courierId) {
        Assert.notNull(courierId, "Courier must not be null");
        this.courierId = courierId;
    }

    public void changeDeliveryStatus(DeliveryStatus deliveryStatus) {
        Assert.notNull(deliveryStatus, "Courier must not be null");
        this.deliveryStatus = deliveryStatus;
    }

    public void StartDelivery() {
        this.startDelivery = Date.from(Instant.now());
    }

    public void removeCourier() {
        if(deliveryStatus != DeliveryStatus.ACCEPTED)
            throw new IllegalStateException("Delivery is already ready for pick, you have to finish the delivery");

        this.courierId = null;
        this.startDelivery = null;
        changeDeliveryStatus(DeliveryStatus.AVAILABLE);
    }
}
