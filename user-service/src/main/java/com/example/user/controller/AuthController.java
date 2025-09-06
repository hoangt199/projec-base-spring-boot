package com.example.user.controller;

import com.example.common.response.ApiResponse;
import com.example.user.dto.AuthRequest;
import com.example.user.dto.AuthResponse;
import com.example.user.dto.ForgotPasswordRequest;
import com.example.user.dto.LogoutRequest;
import com.example.user.dto.RefreshTokenRequest;
import com.example.user.dto.RegisterRequest;
import com.example.user.dto.ResetPasswordRequest;
import com.example.user.service.AuthService;
import com.example.user.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username or email already exists", content = @Content)
    })
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Parameter(description = "User registration request", required = true, schema = @Schema(implementation = RegisterRequest.class))
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user", description = "Authenticates a user with the provided credentials")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Parameter(description = "User authentication request", required = true, schema = @Schema(implementation = AuthRequest.class))
            @Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success("User authenticated successfully", response));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Refreshes the access token using a valid refresh token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid refresh token", content = @Content)
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Parameter(description = "Token refresh request", required = true, schema = @Schema(implementation = RefreshTokenRequest.class))
            @Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout a user from current device", description = "Logs out the currently authenticated user from the current device")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(description = "Device ID", required = false)
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            authService.logout(authentication.getName());
        }
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
    
    @PostMapping("/logout-specific")
    @Operation(summary = "Logout a user from a specific session or device", 
               description = "Logs out the currently authenticated user from a specific session or device")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logged out from specific session/device successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> logoutSpecific(
            @Parameter(description = "Logout request with session or device ID", required = true)
            @Valid @RequestBody LogoutRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            if (request.getSessionId() != null && !request.getSessionId().isEmpty()) {
                authService.logoutBySessionId(authentication.getName(), request.getSessionId());
            } else if (request.getDeviceId() != null && !request.getDeviceId().isEmpty()) {
                authService.logoutByDeviceId(authentication.getName(), request.getDeviceId());
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Either sessionId or deviceId must be provided"));
            }
        }
        return ResponseEntity.ok(ApiResponse.success("Logged out from specific session/device successfully", null));
    }
    
    @PostMapping("/logout-all-devices")
    @Operation(summary = "Logout a user from all devices", description = "Logs out the currently authenticated user from all devices")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logged out from all devices successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> logoutAllDevices() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            authService.logout(authentication.getName()); // This will logout from all devices as no device ID is provided
        }
        return ResponseEntity.ok(ApiResponse.success("Logged out from all devices successfully", null));
    }
    
    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset", description = "Sends a password reset link to the user's email")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset email sent successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Parameter(description = "Forgot password request", required = true, schema = @Schema(implementation = ForgotPasswordRequest.class))
            @Valid @RequestBody ForgotPasswordRequest request) {
        boolean success = passwordResetService.processForgotPasswordRequest(request);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("Password reset instructions sent to your email", null));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to process password reset request"));
        }
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets the user's password using a valid token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or token", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Parameter(description = "Reset password request", required = true, schema = @Schema(implementation = ResetPasswordRequest.class))
            @Valid @RequestBody ResetPasswordRequest request) {
        boolean success = passwordResetService.resetPassword(request);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to reset password"));
        }
    }
}