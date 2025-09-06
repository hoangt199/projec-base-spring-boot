package com.example.user.controller;

import com.example.user.dto.UserModuleRoleDto;
import com.example.user.dto.UserModuleRoleRequest;
import com.example.user.service.UserModuleRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for UserModuleRole management
 */
@RestController
@RequestMapping("/api/v1/user-module-roles")
@RequiredArgsConstructor
public class UserModuleRoleController {
    
    private final UserModuleRoleService userModuleRoleService;
    
    /**
     * Get all user module roles
     * 
     * @return the list of user module roles
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserModuleRoleDto>> getAllUserModuleRoles() {
        return ResponseEntity.ok(userModuleRoleService.getAllUserModuleRoles());
    }
    
    /**
     * Get user module role by ID
     * 
     * @param id the user module role ID
     * @return the user module role
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserModuleRoleDto> getUserModuleRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(userModuleRoleService.getUserModuleRoleById(id));
    }
    
    /**
     * Get user module roles by user ID
     * 
     * @param userId the user ID
     * @return the list of user module roles
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<UserModuleRoleDto>> getUserModuleRolesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(userModuleRoleService.getUserModuleRolesByUserId(userId));
    }
    
    /**
     * Get user module roles by module ID
     * 
     * @param moduleId the module ID
     * @return the list of user module roles
     */
    @GetMapping("/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserModuleRoleDto>> getUserModuleRolesByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(userModuleRoleService.getUserModuleRolesByModuleId(moduleId));
    }
    
    /**
     * Get user module roles by user ID and module ID
     * 
     * @param userId the user ID
     * @param moduleId the module ID
     * @return the list of user module roles
     */
    @GetMapping("/user/{userId}/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<UserModuleRoleDto>> getUserModuleRolesByUserIdAndModuleId(
            @PathVariable UUID userId, @PathVariable UUID moduleId) {
        return ResponseEntity.ok(userModuleRoleService.getUserModuleRolesByUserIdAndModuleId(userId, moduleId));
    }
    
    /**
     * Get user module roles by user ID and module key
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @return the list of user module roles
     */
    @GetMapping("/user/{userId}/module-key/{moduleKey}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<UserModuleRoleDto>> getUserModuleRolesByUserIdAndModuleKey(
            @PathVariable UUID userId, @PathVariable String moduleKey) {
        return ResponseEntity.ok(userModuleRoleService.getUserModuleRolesByUserIdAndModuleKey(userId, moduleKey));
    }
    
    /**
     * Assign a role to a user in a module
     * 
     * @param request the user module role request
     * @return the created user module role
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserModuleRoleDto> assignRoleToUserInModule(@Valid @RequestBody UserModuleRoleRequest request) {
        return new ResponseEntity<>(userModuleRoleService.assignRoleToUserInModule(request), HttpStatus.CREATED);
    }
    
    /**
     * Remove a role from a user in a module
     * 
     * @param id the user module role ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRoleFromUserInModule(@PathVariable UUID id) {
        userModuleRoleService.removeRoleFromUserInModule(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Check if a user has a specific role in a module
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @param roleName the role name
     * @return true if the user has the role in the module, false otherwise
     */
    @GetMapping("/check-role")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Boolean> hasModuleRole(
            @RequestParam UUID userId, 
            @RequestParam String moduleKey, 
            @RequestParam String roleName) {
        return ResponseEntity.ok(userModuleRoleService.hasModuleRole(userId, moduleKey, roleName));
    }
    
    /**
     * Check if a user has a specific permission in a module
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @param permissionName the permission name
     * @return true if the user has the permission in the module, false otherwise
     */
    @GetMapping("/check-permission")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Boolean> hasModulePermission(
            @RequestParam UUID userId, 
            @RequestParam String moduleKey, 
            @RequestParam String permissionName) {
        return ResponseEntity.ok(userModuleRoleService.hasModulePermission(userId, moduleKey, permissionName));
    }
}