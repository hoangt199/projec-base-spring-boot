package com.example.user.service;

import com.example.user.dto.MenuDto;
import com.example.user.dto.request.MenuRequest;
import com.example.user.entity.Menu;
import com.example.user.entity.Module;
import com.example.user.entity.Router;
import com.example.user.exception.ResourceAlreadyExistsException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.MenuMapper;
import com.example.user.repository.MenuRepository;
import com.example.user.repository.ModuleRepository;
import com.example.user.repository.RouterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for Menu management
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModuleRepository moduleRepository;
    private final RouterRepository routerRepository;
    private final MenuMapper menuMapper;

    /**
     * Get all menus
     * @return List of MenuDto
     */
    public List<MenuDto> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active menus
     * @return List of MenuDto
     */
    public List<MenuDto> getAllActiveMenus() {
        List<Menu> menus = menuRepository.findAll().stream()
                .filter(Menu::isActive)
                .toList();
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all visible and active menus
     * @return List of MenuDto
     */
    public List<MenuDto> getAllVisibleActiveMenus() {
        List<Menu> menus = menuRepository.findAll().stream()
                .filter(menu -> menu.isActive() && menu.isVisible())
                .toList();
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get menu by id
     * @param id menu id
     * @return MenuDto
     * @throws ResourceNotFoundException if menu not found
     */
    public MenuDto getMenuById(UUID id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        return menuMapper.toDto(menu);
    }

    /**
     * Get menu by name
     * @param name menu name
     * @return MenuDto
     * @throws ResourceNotFoundException if menu not found
     */
    public MenuDto getMenuByName(String name) {
        Menu menu = menuRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with name: " + name));
        return menuMapper.toDto(menu);
    }

    /**
     * Get all menus by module id
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getMenusByModuleId(UUID moduleId) {
        List<Menu> menus = menuRepository.findByModuleId(moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active menus by module id
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getActiveMenusByModuleId(UUID moduleId) {
        List<Menu> menus = menuRepository.findByModuleIdAndActiveTrue(moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all visible and active menus by module id
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getVisibleActiveMenusByModuleId(UUID moduleId) {
        List<Menu> menus = menuRepository.findByModuleIdAndActiveTrueAndVisibleTrue(moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all menus by router id
     * @param routerId router id
     * @return List of MenuDto
     */
    public List<MenuDto> getMenusByRouterId(UUID routerId) {
        List<Menu> menus = menuRepository.findByRouterId(routerId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active menus by router id
     * @param routerId router id
     * @return List of MenuDto
     */
    public List<MenuDto> getActiveMenusByRouterId(UUID routerId) {
        List<Menu> menus = menuRepository.findByRouterIdAndActiveTrue(routerId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all menus by parent id
     * @param parentId parent menu id
     * @return List of MenuDto
     */
    public List<MenuDto> getMenusByParentId(UUID parentId) {
        List<Menu> menus = menuRepository.findByParentId(parentId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active menus by parent id
     * @param parentId parent menu id
     * @return List of MenuDto
     */
    public List<MenuDto> getActiveMenusByParentId(UUID parentId) {
        List<Menu> menus = menuRepository.findByParentIdAndActiveTrue(parentId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all visible and active menus by parent id
     * @param parentId parent menu id
     * @return List of MenuDto
     */
    public List<MenuDto> getVisibleActiveMenusByParentId(UUID parentId) {
        List<Menu> menus = menuRepository.findByParentIdAndActiveTrueAndVisibleTrue(parentId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all root menus (parentId is null)
     * @return List of MenuDto
     */
    public List<MenuDto> getRootMenus() {
        List<Menu> menus = menuRepository.findByParentIdIsNull();
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active root menus (parentId is null)
     * @return List of MenuDto
     */
    public List<MenuDto> getActiveRootMenus() {
        List<Menu> menus = menuRepository.findByParentIdIsNullAndActiveTrue();
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all visible and active root menus (parentId is null)
     * @return List of MenuDto
     */
    public List<MenuDto> getVisibleActiveRootMenus() {
        List<Menu> menus = menuRepository.findByParentIdIsNullAndActiveTrueAndVisibleTrue();
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all menus by module id and parent id
     * @param moduleId module id
     * @param parentId parent menu id
     * @return List of MenuDto
     */
    public List<MenuDto> getMenusByModuleIdAndParentId(UUID moduleId, UUID parentId) {
        List<Menu> menus = menuRepository.findByModuleIdAndParentId(moduleId, parentId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active menus by module id and parent id
     * @param moduleId module id
     * @param parentId parent menu id
     * @return List of MenuDto
     */
    public List<MenuDto> getActiveMenusByModuleIdAndParentId(UUID moduleId, UUID parentId) {
        List<Menu> menus = menuRepository.findByModuleIdAndParentIdAndActiveTrue(moduleId, parentId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all visible and active menus by module id and parent id
     * @param moduleId module id
     * @param parentId parent menu id
     * @return List of MenuDto
     */
    public List<MenuDto> getVisibleActiveMenusByModuleIdAndParentId(UUID moduleId, UUID parentId) {
        List<Menu> menus = menuRepository.findByModuleIdAndParentIdAndActiveTrueAndVisibleTrue(moduleId, parentId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all root menus by module id (parentId is null)
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getRootMenusByModuleId(UUID moduleId) {
        List<Menu> menus = menuRepository.findByModuleIdAndParentIdIsNull(moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all active root menus by module id (parentId is null)
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getActiveRootMenusByModuleId(UUID moduleId) {
        List<Menu> menus = menuRepository.findByModuleIdAndParentIdIsNullAndActiveTrue(moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all visible and active root menus by module id (parentId is null)
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getVisibleActiveRootMenusByModuleId(UUID moduleId) {
        List<Menu> menus = menuRepository.findByModuleIdAndParentIdIsNullAndActiveTrueAndVisibleTrue(moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all menus by user id
     * @param userId user id
     * @return List of MenuDto
     */
    public List<MenuDto> getMenusByUserId(UUID userId) {
        List<Menu> menus = menuRepository.findAllMenusByUserId(userId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Get all menus by user id and module id
     * @param userId user id
     * @param moduleId module id
     * @return List of MenuDto
     */
    public List<MenuDto> getMenusByUserIdAndModuleId(UUID userId, UUID moduleId) {
        List<Menu> menus = menuRepository.findAllMenusByUserIdAndModuleId(userId, moduleId);
        return menuMapper.toDtoList(menus);
    }

    /**
     * Create a new menu
     * @param request MenuRequest
     * @return MenuDto
     * @throws ResourceAlreadyExistsException if menu with same name already exists
     * @throws ResourceNotFoundException if module or router not found
     */
    @Transactional
    public MenuDto createMenu(MenuRequest request) {
        // Check if menu with same name already exists
        if (menuRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Menu already exists with name: " + request.getName());
        }
        
        // Get module
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));
        
        // Get router if provided
        Router router = null;
        if (request.getRouterId() != null) {
            router = routerRepository.findById(request.getRouterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Router not found with id: " + request.getRouterId()));
        }
        
        // Create menu
        Menu menu = menuMapper.toEntity(request, module, router);
        menu = menuRepository.save(menu);
        
        return menuMapper.toDto(menu);
    }

    /**
     * Update menu
     * @param id menu id
     * @param request MenuRequest
     * @return MenuDto
     * @throws ResourceNotFoundException if menu, module or router not found
     * @throws ResourceAlreadyExistsException if menu with same name already exists
     */
    @Transactional
    public MenuDto updateMenu(UUID id, MenuRequest request) {
        // Get menu
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        
        // Check if menu with same name already exists (excluding current menu)
        if (!menu.getName().equals(request.getName()) && menuRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Menu already exists with name: " + request.getName());
        }
        
        // Get module
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));
        
        // Get router if provided
        Router router = null;
        if (request.getRouterId() != null) {
            router = routerRepository.findById(request.getRouterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Router not found with id: " + request.getRouterId()));
        }
        
        // Update menu
        menu = menuMapper.updateEntity(menu, request, module, router);
        menu = menuRepository.save(menu);
        
        return menuMapper.toDto(menu);
    }

    /**
     * Delete menu
     * @param id menu id
     * @throws ResourceNotFoundException if menu not found
     */
    @Transactional
    public void deleteMenu(UUID id) {
        // Check if menu exists
        if (!menuRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }
        
        // Delete menu
        menuRepository.deleteById(id);
    }

    /**
     * Set menu active status
     * @param id menu id
     * @param active active status
     * @return MenuDto
     * @throws ResourceNotFoundException if menu not found
     */
    @Transactional
    public MenuDto setMenuActive(UUID id, boolean active) {
        // Get menu
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        
        // Set active status
        menu.setActive(active);
        menu = menuRepository.save(menu);
        
        return menuMapper.toDto(menu);
    }

    /**
     * Set menu visible status
     * @param id menu id
     * @param visible visible status
     * @return MenuDto
     * @throws ResourceNotFoundException if menu not found
     */
    @Transactional
    public MenuDto setMenuVisible(UUID id, boolean visible) {
        // Get menu
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        
        // Set visible status
        menu.setVisible(visible);
        menu = menuRepository.save(menu);
        
        return menuMapper.toDto(menu);
    }
}