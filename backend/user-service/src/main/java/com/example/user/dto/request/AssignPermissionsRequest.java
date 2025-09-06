package com.example.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to assign permissions to a role")
public class AssignPermissionsRequest {

    @NotEmpty(message = "At least one permission ID is required")
    @Schema(description = "Set of permission IDs to assign to the role", example = "[\"123e4567-e89b-12d3-a456-426614174000\", \"223e4567-e89b-12d3-a456-426614174000\"]")
    private Set<UUID> permissionIds;
}