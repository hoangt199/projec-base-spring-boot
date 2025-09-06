package com.example.user.controller;

import com.example.user.dto.MenuDto;
import com.example.user.dto.MenuTreeDto;
import com.example.user.dto.ModuleDto;
import com.example.user.dto.ModuleMenuDto;
import com.example.user.dto.RouterDto;
import com.example.user.mapper.MenuTreeMapper;
import com.example.user.service.MenuService;
import com.example.user.service.ModuleService;
import com.example.user.service.RouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing user module menus and routers
 */
@RestController
@RequestMapping("/api/v1/user-module-menu")
@RequiredArgsConstructor
public class UserModuleMenuController {

    private final MenuService menuService;
    private final RouterService routerService;
    private final ModuleService moduleService;
    private final MenuTreeMapper menuTreeMapper;

    /**
     * Get all modules for current user
     * @param userId user id
     * @return List of ModuleDto
     */
    @GetMapping("/modules/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<ModuleDto>> getUserModules(@PathVariable UUID userId) {
        return ResponseEntity.ok(moduleService.getModulesByUserId(userId));
    }

    /**
     * Get all menus for current user
     * @param userId user id
     * @return List of MenuDto
     */
    @GetMapping("/menus/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<MenuDto>> getUserMenus(@PathVariable UUID userId) {
        return ResponseEntity.ok(menuService.getMenusByUserId(userId));
    }

    /**
     * Get all menus for current user by module id
     * @param userId user id
     * @param moduleId module id
     * @return List of MenuDto
     */
    @GetMapping("/menus/user/{userId}/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<MenuDto>> getUserMenusByModule(
            @PathVariable UUID userId,
            @PathVariable UUID moduleId) {
        return ResponseEntity.ok(menuService.getMenusByUserIdAndModuleId(userId, moduleId));
    }

    /**
     * Get all routers for current user
     * @param userId user id
     * @return List of RouterDto
     */
    @GetMapping("/routers/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<RouterDto>> getUserRouters(@PathVariable UUID userId) {
        return ResponseEntity.ok(routerService.getRoutersByUserId(userId));
    }
    
    /**
     * Get all routers for current user by module id
     * @param userId user id
     * @param moduleId module id
     * @return List of RouterDto
     */
    @GetMapping("/routers/user/{userId}/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<RouterDto>> getUserRoutersByModule(
            @PathVariable UUID userId,
            @PathVariable UUID moduleId) {
        // Get all user routers and filter by module ID
        List<RouterDto> moduleRouters = routerService.getRoutersByUserId(userId).stream()
                .filter(router -> router.getModuleId().equals(moduleId))
                .toList();
        return ResponseEntity.ok(moduleRouters);
    }

    /**
     * Get menu tree structure for user
     * @param userId user id
     * @return List of MenuTreeDto with tree structure
     */
    @GetMapping("/menus/tree/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<MenuTreeDto>> getUserMenuTree(@PathVariable UUID userId) {
        List<MenuDto> menus = menuService.getMenusByUserId(userId);
        return ResponseEntity.ok(menuTreeMapper.toMenuTree(menus));
    }
    
    /**
     * Get menu tree structure for user by module id
     * @param userId user id
     * @param moduleId module id
     * @return List of MenuTreeDto with tree structure
     */
    @GetMapping("/menus/tree/user/{userId}/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<MenuTreeDto>> getUserMenuTreeByModule(
            @PathVariable UUID userId,
            @PathVariable UUID moduleId) {
        List<MenuDto> menus = menuService.getMenusByUserIdAndModuleId(userId, moduleId);
        return ResponseEntity.ok(menuTreeMapper.toMenuTree(menus));
    }
    
    /**
     * Get user module menu structure (modules with their menus and routers)
     * @param userId user id
     * @return List of ModuleMenuDto containing module, menu and router information
     */
    @GetMapping("/structure/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<ModuleMenuDto>> getUserModuleMenuStructure(@PathVariable UUID userId) {
        // Get all modules for user
        List<ModuleDto> modules = moduleService.getModulesByUserId(userId);
        
        // Create module menu structure
        List<ModuleMenuDto> structure = modules.stream()
                .map(module -> {
                    // Get menus for this module
                    List<MenuDto> menus = menuService.getMenusByUserIdAndModuleId(userId, module.getId());
                    
                    // Get routers for this module (assuming we need to filter by user permissions)
                    // Since there's no direct method to get routers by user and module, we'll get all user routers
                    // and filter them by module ID
                    List<RouterDto> moduleRouters = routerService.getRoutersByUserId(userId).stream()
                            .filter(router -> router.getModuleId().equals(module.getId()))
                            .toList();
                    
                    // Create menu tree structure
                    List<MenuTreeDto> menuTree = menuTreeMapper.toMenuTree(menus);
                    
                    // Create module menu structure
                    return ModuleMenuDto.builder()
                            .module(module)
                            .menus(menus)
                            .routers(moduleRouters)
                            .menuTree(menuTree)
                            .build();
                })
                .toList();
        
        return ResponseEntity.ok(structure);
    }
}