package be.kdg.sa.deliveryservice.infrastructure.handler;

import be.kdg.sa.deliveryservice.application.DeliveryService;
import be.kdg.sa.deliveryservice.infrastructure.config.RabbitMQTopology;
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

    @RabbitListener(queues = RabbitMQTopology.DELIVERY_ACCEPT_QUEUE)
    public void receiveAcceptedOrderResponse(RestaurantResponse msg) {
        deliveryService.processAcceptedOrder(msg);
    }
    @RabbitListener(queues = RabbitMQTopology.DELIVERY_READY_QUEUE)
    public void receiveReadyOrderResponse(RestaurantResponse msg) {
        deliveryService.processReadyOrder(msg);
    }
}
