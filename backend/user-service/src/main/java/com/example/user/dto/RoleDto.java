package com.example.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role data transfer object")
public class RoleDto {

    @Schema(description = "Role unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private Long id;

    @Schema(description = "Role name", example = "ADMIN")
    private String name;

    @Schema(description = "Role description", example = "Administrator with full access")
    private String description;

    @Schema(description = "Permissions assigned to this role")
    private Set<PermissionDto> permissions;

    @Schema(description = "Creation timestamp", example = "2023-01-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-01-01T12:00:00")
    private LocalDateTime updatedAt;
}