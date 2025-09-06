package com.example.user.service;

import com.example.user.dto.PermissionDto;
import com.example.user.dto.request.CreatePermissionRequest;

import java.util.List;
import java.util.UUID;

public interface PermissionService {
    
    /**
     * Get all permissions
     * @return List of all permissions
     */
    List<PermissionDto> getAllPermissions();
    
    /**
     * Get permission by ID
     * @param id Permission ID
     * @return Permission DTO
     */
    PermissionDto getPermissionById(UUID id);
    
    /**
     * Create a new permission
     * @param request Permission creation request
     * @return Created permission DTO
     */
    PermissionDto createPermission(CreatePermissionRequest request);
    
    /**
     * Delete a permission
     * @param id Permission ID
     */
    void deletePermission(UUID id);
}