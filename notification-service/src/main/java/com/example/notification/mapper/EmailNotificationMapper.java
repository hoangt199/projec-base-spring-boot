package com.example.notification.mapper;

import com.example.notification.dto.EmailNotificationDto;
import com.example.notification.dto.SendEmailRequest;
import com.example.notification.model.EmailNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailNotificationMapper {
    
    EmailNotificationDto toDto(EmailNotification emailNotification);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    EmailNotification toEntity(SendEmailRequest request);
}