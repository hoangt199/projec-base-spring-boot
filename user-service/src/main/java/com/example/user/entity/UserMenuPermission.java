package com.example.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for User Menu Permission
 * Stores information about user permissions for specific menus
 */
@Entity
@Table(name = "user_menu_permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuPermission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    
    @Column(name = "can_view")
    private boolean canView = false;
    
    @Column(name = "can_create")
    private boolean canCreate = false;
    
    @Column(name = "can_edit")
    private boolean canEdit = false;
    
    @Column(name = "can_delete")
    private boolean canDelete = false;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}