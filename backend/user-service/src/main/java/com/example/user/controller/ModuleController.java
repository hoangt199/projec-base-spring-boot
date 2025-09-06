package com.example.user.controller;

import com.example.user.dto.ModuleDto;
import com.example.user.dto.ModuleRequest;
import com.example.user.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Module management
 */
@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {
    
    private final ModuleService moduleService;
    
    /**
     * Get all modules
     * 
     * @return the list of modules
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ModuleDto>> getAllModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }
    
    /**
     * Get all active modules
     * 
     * @return the list of active modules
     */
    @GetMapping("/active")
    public ResponseEntity<List<ModuleDto>> getActiveModules() {
        return ResponseEntity.ok(moduleService.getActiveModules());
    }
    
    /**
     * Get module by ID
     * 
     * @param id the module ID
     * @return the module
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> getModuleById(@PathVariable UUID id) {
        return ResponseEntity.ok(moduleService.getModuleById(id));
    }
    
    /**
     * Get module by key
     * 
     * @param moduleKey the module key
     * @return the module
     */
    @GetMapping("/key/{moduleKey}")
    public ResponseEntity<ModuleDto> getModuleByKey(@PathVariable String moduleKey) {
        return ResponseEntity.ok(moduleService.getModuleByKey(moduleKey));
    }
    
    /**
     * Create a new module
     * 
     * @param request the module request
     * @return the created module
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> createModule(@Valid @RequestBody ModuleRequest request) {
        return new ResponseEntity<>(moduleService.createModule(request), HttpStatus.CREATED);
    }
    
    /**
     * Update a module
     * 
     * @param id the module ID
     * @param request the module request
     * @return the updated module
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> updateModule(@PathVariable UUID id, @Valid @RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleService.updateModule(id, request));
    }
    
    /**
     * Delete a module
     * 
     * @param id the module ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteModule(@PathVariable UUID id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Activate a module
     * 
     * @param id the module ID
     * @return the updated module
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> activateModule(@PathVariable UUID id) {
        return ResponseEntity.ok(moduleService.setModuleActive(id, true));
    }
    
    /**
     * Deactivate a module
     * 
     * @param id the module ID
     * @return the updated module
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> deactivateModule(@PathVariable UUID id) {
        return ResponseEntity.ok(moduleService.setModuleActive(id, false));
    }
    
    /**
     * Get modules by user ID
     * 
     * @param userId the user ID
     * @return the list of modules
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<ModuleDto>> getModulesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(moduleService.getModulesByUserId(userId));
    }
}