package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for UserMenuPermission entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuPermissionDto {
    
    public UUID getId() {
        return id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public UUID getMenuId() {
        return menuId;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public UUID getModuleId() {
        return moduleId;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    
    public String getModuleKey() {
        return moduleKey;
    }
    
    public boolean isCanView() {
        return canView;
    }
    
    public boolean isCanCreate() {
        return canCreate;
    }
    
    public boolean isCanEdit() {
        return canEdit;
    }
    
    public boolean isCanDelete() {
        return canDelete;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    private UUID id;
    private UUID userId;
    private String username;
    private UUID menuId;
    private String menuName;
    private UUID moduleId;
    private String moduleName;
    private String moduleKey;
    private boolean canView;
    private boolean canCreate;
    private boolean canEdit;
    private boolean canDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}