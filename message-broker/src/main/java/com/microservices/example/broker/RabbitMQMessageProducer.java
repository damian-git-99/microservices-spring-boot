package com.microservices.example.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQMessageProducer {

    private final AmqpTemplate amqpTemplate;

    public void publish(String exchange, String routingKey, Object payload) {
        log.info("Publishing message {} to exchange {} with routing key {}", payload, exchange, routingKey);
        amqpTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("Message published successfully to exchange {} with routing key {}", exchange, routingKey);
    }

}
