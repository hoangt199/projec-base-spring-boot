package com.example.file.dto;

import com.example.file.entity.FilePermission.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for file permission
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilePermissionDto {
    private UUID id;
    private UUID fileId;
    private UUID userId;
    private PermissionType permissionType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}