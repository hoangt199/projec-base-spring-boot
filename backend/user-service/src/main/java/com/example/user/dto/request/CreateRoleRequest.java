package com.example.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new role")
public class CreateRoleRequest {
    
    // Explicit getter and setter methods
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
    @Schema(description = "Role name", example = "MANAGER")
    private String name;

    @NotBlank(message = "Role description is required")
    @Size(max = 255, message = "Role description must not exceed 255 characters")
    @Schema(description = "Role description", example = "Manager with access to team resources")
    private String description;
}