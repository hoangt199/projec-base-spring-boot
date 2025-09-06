package com.example.notification.controller;

import com.example.common.dto.ApiResponse;
import com.example.notification.dto.CreateNotificationRequest;
import com.example.notification.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "API for managing notifications")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create a new notification", description = "Creates a new notification with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<ApiResponse<NotificationDto>> createNotification(
            @Parameter(description = "Notification creation request", required = true, schema = @Schema(implementation = CreateNotificationRequest.class))
            @Valid @RequestBody CreateNotificationRequest request) {
        NotificationDto notification = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Notification created successfully", notification));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user notifications with pagination", description = "Retrieves a paginated list of notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<Page<NotificationDto>>> getUserNotifications(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID userId,
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        Page<NotificationDto> notifications = notificationService.getUserNotifications(userId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "User notifications retrieved successfully", notifications));
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications for a user", description = "Retrieves all unread notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getUnreadNotifications(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID userId) {
        List<NotificationDto> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unread notifications retrieved successfully", notifications));
    }

    @GetMapping("/user/{userId}/unread/count")
    @Operation(summary = "Count unread notifications for a user", description = "Counts the number of unread notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread notification count retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<Long>> countUnreadNotifications(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID userId) {
        long count = notificationService.countUnreadNotifications(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unread notification count retrieved successfully", count));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieves a notification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<NotificationDto>> getNotificationById(
            @Parameter(description = "Notification ID", required = true)
            @PathVariable UUID id) {
        NotificationDto notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notification retrieved successfully", notification));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<NotificationDto>> markAsRead(
            @Parameter(description = "Notification ID", required = true)
            @PathVariable UUID id) {
        NotificationDto notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notification marked as read", notification));
    }

    @PatchMapping("/user/{userId}/read-all")
    @Operation(summary = "Mark all notifications as read for a user", description = "Marks all notifications as read for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All notifications marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "All notifications marked as read", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Deletes a notification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @Parameter(description = "Notification ID", required = true)
            @PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notification deleted successfully", null));
    }
}