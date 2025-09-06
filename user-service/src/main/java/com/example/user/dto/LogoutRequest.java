package com.example.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for logout request with optional session ID for specific session logout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {

    @Schema(description = "Session ID to logout from a specific session", example = "123e4567-e89b-12d3-a456-426614174000")
    private String sessionId;
    
    @Schema(description = "Device ID to logout from a specific device", example = "device-123")
    private String deviceId;
}