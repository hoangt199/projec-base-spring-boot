package com.example.file.dto;

import com.example.file.entity.FilePermission.PermissionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for sharing a file
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareFileRequest {
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Permission type is required")
    private PermissionType permissionType;
}