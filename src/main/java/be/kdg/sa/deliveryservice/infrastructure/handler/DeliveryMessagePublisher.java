package be.kdg.sa.deliveryservice.infrastructure.handler;

import be.kdg.sa.deliveryservice.infrastructure.config.RabbitMQTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit.delivery.response.exchange}")
    private String DELIVERY_RESPONSE_EXCHANGE_NAME;

    public DeliveryMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendClaimedResponse(DeliveryResponse response) {
        rabbitTemplate.convertAndSend(
                DELIVERY_RESPONSE_EXCHANGE_NAME,
                "order.claimed." + response.orderId(),
                response
        );
    }
    public void sendPickedUpResponse(DeliveryResponse response) {
        rabbitTemplate.convertAndSend(
                DELIVERY_RESPONSE_EXCHANGE_NAME,
                "order.pickedup." + response.orderId(),
                response
        );
    }

    public void sendDeliveredResponse(DeliveryResponse response) {
        rabbitTemplate.convertAndSend(
                DELIVERY_RESPONSE_EXCHANGE_NAME,
                "order.deliverd." + response.orderId(),
                response
        );
    }
}