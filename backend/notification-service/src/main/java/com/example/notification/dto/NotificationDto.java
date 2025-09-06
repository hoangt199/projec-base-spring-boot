package com.example.notification.dto;

import com.example.notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    
    private UUID id;
    private UUID userId;
    private NotificationType type;
    private String title;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}