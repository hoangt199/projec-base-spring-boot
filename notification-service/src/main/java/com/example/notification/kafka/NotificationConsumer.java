package com.example.notification.kafka;

import com.example.notification.dto.CreateNotificationRequest;
import com.example.notification.dto.SendEmailRequest;
import com.example.notification.service.EmailService;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final EmailService emailService;

    @KafkaListener(topics = "notification-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeNotification(CreateNotificationRequest request) {
        log.info("Received notification request for user: {}", request.getUserId());
        try {
            notificationService.createNotification(request);
            log.info("Successfully processed notification for user: {}", request.getUserId());
        } catch (Exception e) {
            log.error("Error processing notification for user: {}", request.getUserId(), e);
        }
    }

    @KafkaListener(topics = "email-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEmail(SendEmailRequest request) {
        log.info("Received email request for recipient: {}", request.getRecipient());
        try {
            emailService.sendEmail(request);
            log.info("Successfully processed email for recipient: {}", request.getRecipient());
        } catch (Exception e) {
            log.error("Error processing email for recipient: {}", request.getRecipient(), e);
        }
    }
}