package com.microservices.example.notification.rabbitmq;

import com.microservices.example.common.clients.notification.NotificationRequest;
import com.microservices.example.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void consumer(NotificationRequest notificationRequest) {
        log.info("Consumed notification request {}", notificationRequest);
        notificationService.sendNotification(notificationRequest);
    }

}
