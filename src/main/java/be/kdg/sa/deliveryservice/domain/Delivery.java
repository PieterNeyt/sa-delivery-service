package be.kdg.sa.deliveryservice.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
public class Delivery {
    public UUID deliveryId;
    public Date startDelivery;
    public Date endDelivery;
    DeliveryStatus deliveryStatus;
    //Order order?

    public Delivery() {
        this.deliveryId = UUID.randomUUID();
        this.deliveryStatus = DeliveryStatus.AVAILABLE;
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
}
