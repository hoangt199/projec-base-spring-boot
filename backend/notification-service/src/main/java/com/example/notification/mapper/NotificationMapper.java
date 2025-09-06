package com.example.notification.mapper;

import com.example.notification.dto.CreateNotificationRequest;
import com.example.notification.dto.NotificationDto;
import com.example.notification.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    
    NotificationDto toDto(Notification notification);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "read", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "readAt", ignore = true)
    Notification toEntity(CreateNotificationRequest request);
}