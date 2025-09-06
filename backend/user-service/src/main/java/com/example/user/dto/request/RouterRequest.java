package com.example.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating or updating a Router
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterRequest {
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public String getRedirect() {
        return redirect;
    }
    
    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public boolean getHidden() {
        return hidden;
    }
    
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
    public UUID getModuleId() {
        return moduleId;
    }
    
    public void setModuleId(UUID moduleId) {
        this.moduleId = moduleId;
    }
    
    public UUID getParentId() {
        return parentId;
    }
    
    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @NotBlank(message = "Path is required")
    private String path;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String component;
    
    private String redirect;
    
    private String icon;
    
    private boolean hidden;
    
    @NotNull(message = "Module ID is required")
    private UUID moduleId;
    
    private UUID parentId;
    
    private Integer displayOrder;
    
    private boolean active = true;
}