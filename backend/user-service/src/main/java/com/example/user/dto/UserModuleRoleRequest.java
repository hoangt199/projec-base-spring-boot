package com.example.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for assigning a role to a user in a module
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModuleRoleRequest {
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Module ID is required")
    private UUID moduleId;
    
    @NotNull(message = "Role ID is required")
    private UUID roleId;
}