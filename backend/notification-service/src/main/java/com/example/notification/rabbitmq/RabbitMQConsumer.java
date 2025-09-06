package com.example.notification.rabbitmq;

import com.example.notification.config.RabbitMQConfig;
import com.example.notification.dto.CreateNotificationRequest;
import com.example.notification.dto.SendEmailRequest;
import com.example.notification.service.EmailService;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {
    private static final org.slf4j.Logger loggger = org.slf4j.LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final NotificationService notificationService;
    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void consumeNotification(CreateNotificationRequest request) {
        log.info("Received notification request from RabbitMQ for user: {}", request.getUserId());
        try {
            notificationService.createNotification(request);
            log.info("Successfully processed notification from RabbitMQ for user: {}", request.getUserId());
        } catch (Exception e) {
            log.error("Error processing notification from RabbitMQ for user: {}", request.getUserId(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEmail(SendEmailRequest request) {
        log.info("Received email request from RabbitMQ for recipient: {}", request.getRecipient());
        try {
            emailService.sendEmail(request);
            log.info("Successfully processed email from RabbitMQ for recipient: {}", request.getRecipient());
        } catch (Exception e) {
            log.error("Error processing email from RabbitMQ for recipient: {}", request.getRecipient(), e);
            // Retry logic or error handling could be added here
        }
    }
}