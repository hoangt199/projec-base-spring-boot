package com.example.user.service;

import com.example.user.dto.UserMenuPermissionDto;
import com.example.user.dto.request.UserMenuPermissionRequest;
import com.example.user.entity.Menu;
import com.example.user.entity.User;
import com.example.user.entity.UserMenuPermission;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.UserMenuPermissionMapper;
import com.example.user.repository.MenuRepository;
import com.example.user.repository.UserMenuPermissionRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for UserMenuPermission management
 */
@Service
@RequiredArgsConstructor
public class UserMenuPermissionService {

    private final UserMenuPermissionRepository userMenuPermissionRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final UserMenuPermissionMapper userMenuPermissionMapper;

    /**
     * Get all user menu permissions
     * @return List of UserMenuPermissionDto
     */
    public List<UserMenuPermissionDto> getAllUserMenuPermissions() {
        List<UserMenuPermission> permissions = userMenuPermissionRepository.findAll();
        return userMenuPermissionMapper.toDtoList(permissions);
    }

    /**
     * Get user menu permission by id
     * @param id permission id
     * @return UserMenuPermissionDto
     * @throws ResourceNotFoundException if permission not found
     */
    public UserMenuPermissionDto getUserMenuPermissionById(UUID id) {
        UserMenuPermission permission = userMenuPermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User menu permission not found with id: " + id));
        return userMenuPermissionMapper.toDto(permission);
    }

    /**
     * Get all user menu permissions by user id
     * @param userId user id
     * @return List of UserMenuPermissionDto
     */
    public List<UserMenuPermissionDto> getUserMenuPermissionsByUserId(UUID userId) {
        List<UserMenuPermission> permissions = userMenuPermissionRepository.findByUserId(userId);
        return userMenuPermissionMapper.toDtoList(permissions);
    }

    /**
     * Get all user menu permissions by menu id
     * @param menuId menu id
     * @return List of UserMenuPermissionDto
     */
    public List<UserMenuPermissionDto> getUserMenuPermissionsByMenuId(UUID menuId) {
        List<UserMenuPermission> permissions = userMenuPermissionRepository.findByMenuId(menuId);
        return userMenuPermissionMapper.toDtoList(permissions);
    }

    /**
     * Get user menu permission by user id and menu id
     * @param userId user id
     * @param menuId menu id
     * @return UserMenuPermissionDto or null if not found
     */
    public UserMenuPermissionDto getUserMenuPermissionByUserIdAndMenuId(UUID userId, UUID menuId) {
        Optional<UserMenuPermission> permission = userMenuPermissionRepository.findByUserIdAndMenuId(userId, menuId);
        return permission.map(userMenuPermissionMapper::toDto).orElse(null);
    }

    /**
     * Get all user menu permissions by user id and module id
     * @param userId user id
     * @param moduleId module id
     * @return List of UserMenuPermissionDto
     */
    public List<UserMenuPermissionDto> getUserMenuPermissionsByUserIdAndModuleId(UUID userId, UUID moduleId) {
        List<UserMenuPermission> permissions = userMenuPermissionRepository.findByUserIdAndModuleId(userId, moduleId);
        return userMenuPermissionMapper.toDtoList(permissions);
    }

    /**
     * Assign menu permission to user
     * @param request UserMenuPermissionRequest
     * @return UserMenuPermissionDto
     * @throws ResourceNotFoundException if user or menu not found
     */
    @Transactional
    public UserMenuPermissionDto assignMenuPermissionToUser(UserMenuPermissionRequest request) {
        // Get user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        
        // Get menu
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + request.getMenuId()));
        
        // Check if permission already exists
        Optional<UserMenuPermission> existingPermission = userMenuPermissionRepository.findByUserIdAndMenuId(request.getUserId(), request.getMenuId());
        
        UserMenuPermission permission;
        if (existingPermission.isPresent()) {
            // Update existing permission
            permission = userMenuPermissionMapper.updateEntity(existingPermission.get(), request);
        } else {
            // Create new permission
            permission = userMenuPermissionMapper.toEntity(request, user, menu);
        }
        
        permission = userMenuPermissionRepository.save(permission);
        
        return userMenuPermissionMapper.toDto(permission);
    }

    /**
     * Remove menu permission from user
     * @param userId user id
     * @param menuId menu id
     * @throws ResourceNotFoundException if permission not found
     */
    @Transactional
    public void removeMenuPermissionFromUser(UUID userId, UUID menuId) {
        // Check if permission exists
        if (!userMenuPermissionRepository.existsByUserIdAndMenuId(userId, menuId)) {
            throw new ResourceNotFoundException("User menu permission not found with userId: " + userId + " and menuId: " + menuId);
        }
        
        // Delete permission
        userMenuPermissionRepository.deleteByUserIdAndMenuId(userId, menuId);
    }

    /**
     * Remove all menu permissions from user
     * @param userId user id
     */
    @Transactional
    public void removeAllMenuPermissionsFromUser(UUID userId) {
        userMenuPermissionRepository.deleteByUserId(userId);
    }

    /**
     * Remove all user permissions from menu
     * @param menuId menu id
     */
    @Transactional
    public void removeAllUserPermissionsFromMenu(UUID menuId) {
        userMenuPermissionRepository.deleteByMenuId(menuId);
    }

    /**
     * Check if user has view permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    public boolean hasViewPermission(UUID userId, UUID menuId) {
        return userMenuPermissionRepository.existsByUserIdAndMenuIdAndCanViewTrue(userId, menuId);
    }

    /**
     * Check if user has create permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    public boolean hasCreatePermission(UUID userId, UUID menuId) {
        return userMenuPermissionRepository.existsByUserIdAndMenuIdAndCanCreateTrue(userId, menuId);
    }

    /**
     * Check if user has edit permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    public boolean hasEditPermission(UUID userId, UUID menuId) {
        return userMenuPermissionRepository.existsByUserIdAndMenuIdAndCanEditTrue(userId, menuId);
    }

    /**
     * Check if user has delete permission for menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    public boolean hasDeletePermission(UUID userId, UUID menuId) {
        return userMenuPermissionRepository.existsByUserIdAndMenuIdAndCanDeleteTrue(userId, menuId);
    }
}