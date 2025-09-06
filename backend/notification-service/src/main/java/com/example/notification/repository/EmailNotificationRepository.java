package com.example.notification.repository;

import com.example.notification.model.EmailNotification;
import com.example.notification.model.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, UUID> {
    
    List<EmailNotification> findByStatus(EmailStatus status);
    
    List<EmailNotification> findByRecipientAndStatus(String recipient, EmailStatus status);
}