package be.kdg.sa.deliveryservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    public static final String RESTAURANT_RESPONSE_EXCHANGE_NAME = "restaurant-response-exchange";
    public static final String DELIVERY_RESPONSE_EXCHANGE_NAME = "delivery-response-exchange";

    public static final String DELIVERY_ACCEPT_QUEUE = "delivery-accept-queue";
    public static final String DELIVERY_READY_QUEUE = "delivery-ready-queue";

    @Bean
    TopicExchange restaurantResponseExchange() {
        return new TopicExchange(RESTAURANT_RESPONSE_EXCHANGE_NAME, true, false);
    }

    // Exchange voor verzenden van DELIVERY updates
    @Bean
    TopicExchange deliveryResponseExchange() {
        return new TopicExchange(DELIVERY_RESPONSE_EXCHANGE_NAME, true, false);
    }

    @Bean
    Queue deliveryAcceptQueue() {
        return QueueBuilder.nonDurable(DELIVERY_ACCEPT_QUEUE).build();
    }

    @Bean
    Queue deliveryReadyQueue() {
        return QueueBuilder.nonDurable(DELIVERY_READY_QUEUE).build();
    }

    @Bean
    Binding deliveryAcceptBinding() {
        return BindingBuilder.bind(deliveryAcceptQueue())
                .to(restaurantResponseExchange())
                .with("order.accept.*");
    }

    @Bean
    Binding deliveryReadyBinding() {
        return BindingBuilder.bind(deliveryReadyQueue())
                .to(restaurantResponseExchange())
                .with("order.ready.*");
    }
}

