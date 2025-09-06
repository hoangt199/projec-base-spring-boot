package com.example.user.controller;

import com.example.user.dto.UserMenuPermissionDto;
import com.example.user.dto.request.UserMenuPermissionRequest;
import com.example.user.service.UserMenuPermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing user menu permissions
 */
@RestController
@RequestMapping("/api/v1/user-menu-permissions")
@RequiredArgsConstructor
public class UserMenuPermissionController {

    private final UserMenuPermissionService userMenuPermissionService;

    /**
     * Get all user menu permissions
     * @return List of UserMenuPermissionDto
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserMenuPermissionDto>> getAllUserMenuPermissions() {
        return ResponseEntity.ok(userMenuPermissionService.getAllUserMenuPermissions());
    }

    /**
     * Get user menu permission by id
     * @param id permission id
     * @return UserMenuPermissionDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userMenuPermissionService.getUserMenuPermissionById(#id).userId == authentication.principal.id")
    public ResponseEntity<UserMenuPermissionDto> getUserMenuPermissionById(@PathVariable UUID id) {
        return ResponseEntity.ok(userMenuPermissionService.getUserMenuPermissionById(id));
    }

    /**
     * Get all user menu permissions by user id
     * @param userId user id
     * @return List of UserMenuPermissionDto
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<UserMenuPermissionDto>> getUserMenuPermissionsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(userMenuPermissionService.getUserMenuPermissionsByUserId(userId));
    }

    /**
     * Get all user menu permissions by menu id
     * @param menuId menu id
     * @return List of UserMenuPermissionDto
     */
    @GetMapping("/menu/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserMenuPermissionDto>> getUserMenuPermissionsByMenuId(@PathVariable UUID menuId) {
        return ResponseEntity.ok(userMenuPermissionService.getUserMenuPermissionsByMenuId(menuId));
    }

    /**
     * Get user menu permission by user id and menu id
     * @param userId user id
     * @param menuId menu id
     * @return UserMenuPermissionDto
     */
    @GetMapping("/user/{userId}/menu/{menuId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<UserMenuPermissionDto> getUserMenuPermissionByUserIdAndMenuId(
            @PathVariable UUID userId, 
            @PathVariable UUID menuId) {
        UserMenuPermissionDto permission = userMenuPermissionService.getUserMenuPermissionByUserIdAndMenuId(userId, menuId);
        if (permission == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(permission);
    }

    /**
     * Get all user menu permissions by user id and module id
     * @param userId user id
     * @param moduleId module id
     * @return List of UserMenuPermissionDto
     */
    @GetMapping("/user/{userId}/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<UserMenuPermissionDto>> getUserMenuPermissionsByUserIdAndModuleId(
            @PathVariable UUID userId, 
            @PathVariable UUID moduleId) {
        return ResponseEntity.ok(userMenuPermissionService.getUserMenuPermissionsByUserIdAndModuleId(userId, moduleId));
    }

    /**
     * Check if user has view permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    @GetMapping("/check/view")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Boolean> hasViewPermission(
            @RequestParam UUID userId, 
            @RequestParam UUID menuId) {
        return ResponseEntity.ok(userMenuPermissionService.hasViewPermission(userId, menuId));
    }

    /**
     * Check if user has create permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    @GetMapping("/check/create")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Boolean> hasCreatePermission(
            @RequestParam UUID userId, 
            @RequestParam UUID menuId) {
        return ResponseEntity.ok(userMenuPermissionService.hasCreatePermission(userId, menuId));
    }

    /**
     * Check if user has edit permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    @GetMapping("/check/edit")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Boolean> hasEditPermission(
            @RequestParam UUID userId, 
            @RequestParam UUID menuId) {
        return ResponseEntity.ok(userMenuPermissionService.hasEditPermission(userId, menuId));
    }

    /**
     * Check if user has delete permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    @GetMapping("/check/delete")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Boolean> hasDeletePermission(
            @RequestParam UUID userId, 
            @RequestParam UUID menuId) {
        return ResponseEntity.ok(userMenuPermissionService.hasDeletePermission(userId, menuId));
    }

    /**
     * Assign menu permission to user
     * @param request UserMenuPermissionRequest
     * @return UserMenuPermissionDto
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or #request.userId == authentication.principal.id")
    public ResponseEntity<UserMenuPermissionDto> assignMenuPermissionToUser(
            @Valid @RequestBody UserMenuPermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMenuPermissionService.assignMenuPermissionToUser(request));
    }

    /**
     * Update menu permission
     * @param id permission id
     * @param request UserMenuPermissionRequest
     * @return UserMenuPermissionDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userMenuPermissionService.getUserMenuPermissionById(#id).userId == authentication.principal.id")
    public ResponseEntity<UserMenuPermissionDto> updateMenuPermission(
            @PathVariable UUID id,
            @Valid @RequestBody UserMenuPermissionRequest request) {
        // Get existing permission to check if it exists
        userMenuPermissionService.getUserMenuPermissionById(id);
        return ResponseEntity.ok(userMenuPermissionService.assignMenuPermissionToUser(request));
    }

    /**
     * Remove menu permission from user
     * @param id permission id
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userMenuPermissionService.getUserMenuPermissionById(#id).userId == authentication.principal.id")
    public ResponseEntity<Void> removeMenuPermission(@PathVariable UUID id) {
        UserMenuPermissionDto permission = userMenuPermissionService.getUserMenuPermissionById(id);
        userMenuPermissionService.removeMenuPermissionFromUser(permission.getUserId(), permission.getMenuId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove menu permission from user by user id and menu id
     * @param userId user id
     * @param menuId menu id
     * @return no content
     */
    @DeleteMapping("/user/{userId}/menu/{menuId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Void> removeMenuPermissionByUserIdAndMenuId(
            @PathVariable UUID userId, 
            @PathVariable UUID menuId) {
        userMenuPermissionService.removeMenuPermissionFromUser(userId, menuId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove all menu permissions from user
     * @param userId user id
     * @return no content
     */
    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAllMenuPermissionsFromUser(@PathVariable UUID userId) {
        userMenuPermissionService.removeAllMenuPermissionsFromUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove all user permissions from menu
     * @param menuId menu id
     * @return no content
     */
    @DeleteMapping("/menu/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAllUserPermissionsFromMenu(@PathVariable UUID menuId) {
        userMenuPermissionService.removeAllUserPermissionsFromMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}