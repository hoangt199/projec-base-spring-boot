package com.example.user.controller;

import com.example.user.dto.RouterDto;
import com.example.user.dto.request.RouterRequest;
import com.example.user.service.RouterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Router management
 */
@RestController
@RequestMapping("/api/routers")
@RequiredArgsConstructor
@Tag(name = "Router Management", description = "APIs for managing routers")
public class RouterController {

    private final RouterService routerService;

    /**
     * Get all routers
     * @return List of RouterDto
     */
    @GetMapping
    @Operation(summary = "Get all routers", description = "Get all routers in the system")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouterDto>> getAllRouters() {
        return ResponseEntity.ok(routerService.getAllRouters());
    }

    /**
     * Get all active routers
     * @return List of RouterDto
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active routers", description = "Get all active routers in the system")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getAllActiveRouters() {
        return ResponseEntity.ok(routerService.getAllActiveRouters());
    }

    /**
     * Get router by id
     * @param id router id
     * @return RouterDto
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get router by id", description = "Get router by id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<RouterDto> getRouterById(@PathVariable UUID id) {
        return ResponseEntity.ok(routerService.getRouterById(id));
    }

    /**
     * Get router by path
     * @param path router path
     * @return RouterDto
     */
    @GetMapping("/path/{path}")
    @Operation(summary = "Get router by path", description = "Get router by path")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<RouterDto> getRouterByPath(@PathVariable String path) {
        return ResponseEntity.ok(routerService.getRouterByPath(path));
    }

    /**
     * Get router by name
     * @param name router name
     * @return RouterDto
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Get router by name", description = "Get router by name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<RouterDto> getRouterByName(@PathVariable String name) {
        return ResponseEntity.ok(routerService.getRouterByName(name));
    }

    /**
     * Get all routers by module id
     * @param moduleId module id
     * @return List of RouterDto
     */
    @GetMapping("/module/{moduleId}")
    @Operation(summary = "Get all routers by module id", description = "Get all routers by module id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getRoutersByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(routerService.getRoutersByModuleId(moduleId));
    }

    /**
     * Get all active routers by module id
     * @param moduleId module id
     * @return List of RouterDto
     */
    @GetMapping("/module/{moduleId}/active")
    @Operation(summary = "Get all active routers by module id", description = "Get all active routers by module id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getActiveRoutersByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(routerService.getActiveRoutersByModuleId(moduleId));
    }

    /**
     * Get all routers by parent id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    @GetMapping("/parent/{parentId}")
    @Operation(summary = "Get all routers by parent id", description = "Get all routers by parent id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getRoutersByParentId(@PathVariable UUID parentId) {
        return ResponseEntity.ok(routerService.getRoutersByParentId(parentId));
    }

    /**
     * Get all active routers by parent id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    @GetMapping("/parent/{parentId}/active")
    @Operation(summary = "Get all active routers by parent id", description = "Get all active routers by parent id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getActiveRoutersByParentId(@PathVariable UUID parentId) {
        return ResponseEntity.ok(routerService.getActiveRoutersByParentId(parentId));
    }

    /**
     * Get all root routers (parentId is null)
     * @return List of RouterDto
     */
    @GetMapping("/root")
    @Operation(summary = "Get all root routers", description = "Get all root routers (parentId is null)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getRootRouters() {
        return ResponseEntity.ok(routerService.getRootRouters());
    }

    /**
     * Get all active root routers (parentId is null)
     * @return List of RouterDto
     */
    @GetMapping("/root/active")
    @Operation(summary = "Get all active root routers", description = "Get all active root routers (parentId is null)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getActiveRootRouters() {
        return ResponseEntity.ok(routerService.getActiveRootRouters());
    }

    /**
     * Get all routers by module id and parent id
     * @param moduleId module id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    @GetMapping("/module/{moduleId}/parent/{parentId}")
    @Operation(summary = "Get all routers by module id and parent id", description = "Get all routers by module id and parent id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getRoutersByModuleIdAndParentId(
            @PathVariable UUID moduleId,
            @PathVariable UUID parentId) {
        return ResponseEntity.ok(routerService.getRoutersByModuleIdAndParentId(moduleId, parentId));
    }

    /**
     * Get all active routers by module id and parent id
     * @param moduleId module id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    @GetMapping("/module/{moduleId}/parent/{parentId}/active")
    @Operation(summary = "Get all active routers by module id and parent id", description = "Get all active routers by module id and parent id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getActiveRoutersByModuleIdAndParentId(
            @PathVariable UUID moduleId,
            @PathVariable UUID parentId) {
        return ResponseEntity.ok(routerService.getActiveRoutersByModuleIdAndParentId(moduleId, parentId));
    }

    /**
     * Get all root routers by module id (parentId is null)
     * @param moduleId module id
     * @return List of RouterDto
     */
    @GetMapping("/module/{moduleId}/root")
    @Operation(summary = "Get all root routers by module id", description = "Get all root routers by module id (parentId is null)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getRootRoutersByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(routerService.getRootRoutersByModuleId(moduleId));
    }

    /**
     * Get all active root routers by module id (parentId is null)
     * @param moduleId module id
     * @return List of RouterDto
     */
    @GetMapping("/module/{moduleId}/root/active")
    @Operation(summary = "Get all active root routers by module id", description = "Get all active root routers by module id (parentId is null)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<RouterDto>> getActiveRootRoutersByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(routerService.getActiveRootRoutersByModuleId(moduleId));
    }

    /**
     * Get all routers by user id
     * @param userId user id
     * @return List of RouterDto
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all routers by user id", description = "Get all routers by user id")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(#userId)")
    public ResponseEntity<List<RouterDto>> getRoutersByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(routerService.getRoutersByUserId(userId));
    }

    /**
     * Create a new router
     * @param request RouterRequest
     * @return RouterDto
     */
    @PostMapping
    @Operation(summary = "Create a new router", description = "Create a new router")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouterDto> createRouter(@Valid @RequestBody RouterRequest request) {
        return new ResponseEntity<>(routerService.createRouter(request), HttpStatus.CREATED);
    }

    /**
     * Update router
     * @param id router id
     * @param request RouterRequest
     * @return RouterDto
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update router", description = "Update router")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouterDto> updateRouter(
            @PathVariable UUID id,
            @Valid @RequestBody RouterRequest request) {
        return ResponseEntity.ok(routerService.updateRouter(id, request));
    }

    /**
     * Delete router
     * @param id router id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete router", description = "Delete router")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRouter(@PathVariable UUID id) {
        routerService.deleteRouter(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Activate router
     * @param id router id
     * @return RouterDto
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate router", description = "Activate router")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouterDto> activateRouter(@PathVariable UUID id) {
        return ResponseEntity.ok(routerService.setRouterActive(id, true));
    }

    /**
     * Deactivate router
     * @param id router id
     * @return RouterDto
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate router", description = "Deactivate router")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouterDto> deactivateRouter(@PathVariable UUID id) {
        return ResponseEntity.ok(routerService.setRouterActive(id, false));
    }
}