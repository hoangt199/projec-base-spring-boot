package com.example.notification.controller;

import com.example.common.dto.ApiResponse;
import com.example.notification.dto.EmailNotificationDto;
import com.example.notification.dto.SendEmailRequest;
import com.example.notification.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Tag(name = "Email Controller", description = "API for managing email notifications")
@SecurityRequirement(name = "bearerAuth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    @Operation(summary = "Send an email")
    public ResponseEntity<ApiResponse<Void>> sendEmail(
            @Valid @RequestBody SendEmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(true, "Email queued for sending", null));
    }

    @PostMapping("/queue")
    @Operation(summary = "Queue an email without sending it immediately")
    public ResponseEntity<ApiResponse<EmailNotificationDto>> queueEmail(
            @Valid @RequestBody SendEmailRequest request) {
        EmailNotificationDto emailNotification = emailService.createEmailNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Email queued successfully", emailNotification));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending emails")
    public ResponseEntity<ApiResponse<List<EmailNotificationDto>>> getPendingEmails() {
        List<EmailNotificationDto> pendingEmails = emailService.getPendingEmails();
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending emails retrieved successfully", pendingEmails));
    }

    @GetMapping("/failed")
    @Operation(summary = "Get all failed emails")
    public ResponseEntity<ApiResponse<List<EmailNotificationDto>>> getFailedEmails() {
        List<EmailNotificationDto> failedEmails = emailService.getFailedEmails();
        return ResponseEntity.ok(new ApiResponse<>(true, "Failed emails retrieved successfully", failedEmails));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get email notification by ID")
    public ResponseEntity<ApiResponse<EmailNotificationDto>> getEmailNotificationById(
            @PathVariable UUID id) {
        EmailNotificationDto emailNotification = emailService.getEmailNotificationById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email notification retrieved successfully", emailNotification));
    }

    @PostMapping("/{id}/retry")
    @Operation(summary = "Retry sending a failed email")
    public ResponseEntity<ApiResponse<Void>> retryFailedEmail(
            @PathVariable UUID id) {
        emailService.retryFailedEmail(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email retry initiated", null));
    }
}