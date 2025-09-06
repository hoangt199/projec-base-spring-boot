package com.example.file.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Feign client for Notification Service
 */
@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

    /**
     * Send notification
     *
     * @param notification the notification data
     * @return the response
     */
    @PostMapping("/api/notifications")
    Map<String, Object> sendNotification(@RequestBody Map<String, Object> notification);
}