package com.ead.notification.consumers;

import com.ead.notification.dtos.NotificationCommandDto;
import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.notificationCommandQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.notificationCommandExchange.name}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "${ead.broker.key.notificationCommandKey.name}"
    ))
    public void listen(@Payload byte[] messageBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NotificationCommandDto notificationCommandDto = objectMapper.readValue(messageBody, NotificationCommandDto.class);
            var notificationModel = new NotificationModel();
            BeanUtils.copyProperties(notificationCommandDto, notificationModel);
            notificationModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            notificationModel.setNotificationStatus(NotificationStatus.CREATED);
            notificationService.saveNotification(notificationModel);
        } catch (IOException e) {
        // Log the error
        System.err.println("Failed to deserialize message: " + e.getMessage());
        throw new AmqpRejectAndDontRequeueException("Deserialization failed", e);
    }
    }

}
