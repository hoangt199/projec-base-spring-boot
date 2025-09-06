package com.example.user.controller;

import com.example.user.dto.RoleDto;
import com.example.user.dto.request.AssignPermissionsRequest;
import com.example.user.dto.request.CreateRoleRequest;
import com.example.user.dto.request.UpdateRoleRequest;
import com.example.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "APIs for managing roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieves a list of all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieves a role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<RoleDto> getRoleById(
            @Parameter(description = "Role ID", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new role", description = "Creates a new role with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "Role already exists", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<RoleDto> createRole(
            @Parameter(description = "Role creation request", required = true, schema = @Schema(implementation = CreateRoleRequest.class))
            @Valid @RequestBody CreateRoleRequest request) {
        return new ResponseEntity<>(roleService.createRole(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a role", description = "Updates an existing role with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Role name already exists", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<RoleDto> updateRole(
            @Parameter(description = "Role ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Role update request", required = true, schema = @Schema(implementation = UpdateRoleRequest.class))
            @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role", description = "Deletes a role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "Role ID", required = true)
            @PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    @Operation(summary = "Assign permissions to a role", description = "Assigns permissions to an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role or permission not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PreAuthorize("hasAuthority('permission:assign')")
    public ResponseEntity<RoleDto> assignPermissionsToRole(
            @Parameter(description = "Role ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Permissions assignment request", required = true, schema = @Schema(implementation = AssignPermissionsRequest.class))
            @Valid @RequestBody AssignPermissionsRequest request) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(id, request));
    }
}