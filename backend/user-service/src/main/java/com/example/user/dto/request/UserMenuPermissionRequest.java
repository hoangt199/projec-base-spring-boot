package com.example.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating or updating a UserMenuPermission
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuPermissionRequest {
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public UUID getMenuId() {
        return menuId;
    }
    
    public void setMenuId(UUID menuId) {
        this.menuId = menuId;
    }
    
    public boolean isCanView() {
        return canView;
    }
    
    public void setCanView(boolean canView) {
        this.canView = canView;
    }
    
    public boolean isCanCreate() {
        return canCreate;
    }
    
    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }
    
    public boolean isCanEdit() {
        return canEdit;
    }
    
    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
    
    public boolean isCanDelete() {
        return canDelete;
    }
    
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Menu ID is required")
    private UUID menuId;
    
    private boolean canView = false;
    
    private boolean canCreate = false;
    
    private boolean canEdit = false;
    
    private boolean canDelete = false;
}