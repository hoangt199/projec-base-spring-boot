package com.example.user.service;

import com.example.user.dto.RoleDto;
import com.example.user.dto.request.AssignPermissionsRequest;
import com.example.user.dto.request.CreateRoleRequest;
import com.example.user.dto.request.UpdateRoleRequest;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    
    /**
     * Get all roles
     * @return List of all roles
     */
    List<RoleDto> getAllRoles();
    
    /**
     * Get role by ID
     * @param id Role ID
     * @return Role DTO
     */
    RoleDto getRoleById(UUID id);
    
    /**
     * Create a new role
     * @param request Role creation request
     * @return Created role DTO
     */
    RoleDto createRole(CreateRoleRequest request);
    
    /**
     * Update an existing role
     * @param id Role ID
     * @param request Role update request
     * @return Updated role DTO
     */
    RoleDto updateRole(UUID id, UpdateRoleRequest request);
    
    /**
     * Delete a role
     * @param id Role ID
     */
    void deleteRole(UUID id);
    
    /**
     * Assign permissions to a role
     * @param roleId Role ID
     * @param request Permissions assignment request
     * @return Updated role DTO with assigned permissions
     */
    RoleDto assignPermissionsToRole(UUID roleId, AssignPermissionsRequest request);
}