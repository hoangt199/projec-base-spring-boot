package com.example.notification.repository;

import com.example.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    Page<Notification> findByUserId(UUID userId, Pageable pageable);
    
    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(UUID userId, boolean read);
    
    long countByUserIdAndRead(UUID userId, boolean read);
}