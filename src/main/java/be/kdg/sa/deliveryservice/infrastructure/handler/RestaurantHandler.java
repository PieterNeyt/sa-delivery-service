package be.kdg.sa.deliveryservice.infrastructure.handler;

import be.kdg.sa.deliveryservice.application.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestaurantHandler {

    private final DeliveryService deliveryService;

    public RestaurantHandler(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RabbitListener(queues = {"${rabbit.delivery.accept.queue}"})
    public void receiveAcceptedOrderResponse(RestaurantResponse msg) {
        deliveryService.processAcceptedOrder(msg);
    }
    @RabbitListener(queues = {"${rabbit.delivery.ready.queue}"})
    public void receiveReadyOrderResponse(RestaurantResponse msg) {
        deliveryService.processReadyOrder(msg);
    }
}
