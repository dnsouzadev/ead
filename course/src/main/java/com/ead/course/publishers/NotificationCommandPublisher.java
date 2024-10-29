package com.ead.course.publishers;

import com.ead.course.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

    RabbitTemplate rabbitTemplate;

    public NotificationCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${ead.broker.exchange.notificationCommandExchange.name}")
    private String notificationCommandExchange;

    @Value("${ead.broker.key.notificationCommandKey.name}")
    private String notificationCommandKey;

    public void publishNotificationCommand(NotificationCommandDto notificationCommandDto) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDto);
    }

}
