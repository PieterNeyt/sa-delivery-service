package be.kdg.sa.deliveryservice.infrastructure.handler;

import be.kdg.sa.deliveryservice.infrastructure.config.RabbitMQTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestaurantHandler {

    @RabbitListener(queues = RabbitMQTopology.DELIVERY_ACCEPT_QUEUE)
    public void receiveAcceptedOrderResponse(RestaurantResponse msg) {
        log.info("Received Order accepted Message: {}", msg);
    }
    @RabbitListener(queues = RabbitMQTopology.DELIVERY_READY_QUEUE)
    public void receiveReadyOrderResponse(RestaurantResponse msg) {
        log.info("Received Order ready Message: {}", msg);
    }
}
