package com.example.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a Module
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleRequest {
    
    public String getName() {
        return name;
    }
    
    public String getModuleKey() {
        return moduleKey;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public boolean getActive() {
        return isActive;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    @NotBlank(message = "Module name is required")
    @Size(min = 3, max = 100, message = "Module name must be between 3 and 100 characters")
    private String name;
    
    @NotBlank(message = "Module key is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Module key can only contain letters, numbers, underscores and hyphens")
    @Size(min = 3, max = 50, message = "Module key must be between 3 and 50 characters")
    private String moduleKey;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private boolean isActive = true;
}