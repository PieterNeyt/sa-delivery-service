package be.kdg.sa.deliveryservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    @Value("${rabbit.restaurant.response.exchange}")
    public String RESTAURANT_RESPONSE_EXCHANGE_NAME;

    @Value("${rabbit.delivery.response.exchange}")
    public String DELIVERY_RESPONSE_EXCHANGE_NAME;

    @Value("${rabbit.delivery.accept.queue}")
    public String DELIVERY_ACCEPT_QUEUE;

    @Value("${rabbit.delivery.ready.queue}")
    public String DELIVERY_READY_QUEUE;

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

