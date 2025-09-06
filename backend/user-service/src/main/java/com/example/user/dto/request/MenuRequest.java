package com.example.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating or updating a Menu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public UUID getRouterId() {
        return routerId;
    }
    
    public UUID getModuleId() {
        return moduleId;
    }
    
    public UUID getParentId() {
        return parentId;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean getActive() {
        return active;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    private String icon;
    
    private UUID routerId;
    
    @NotNull(message = "Module ID is required")
    private UUID moduleId;
    
    private UUID parentId;
    
    private Integer displayOrder;
    
    private boolean active = true;
    
    private boolean visible = true;
}