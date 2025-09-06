package com.example.user.mapper;

import com.example.user.dto.UserMenuPermissionDto;
import com.example.user.dto.request.UserMenuPermissionRequest;
import com.example.user.entity.Menu;
import com.example.user.entity.User;
import com.example.user.entity.UserMenuPermission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for UserMenuPermission entity and DTOs
 */
@Component
public class UserMenuPermissionMapper {

    /**
     * Convert UserMenuPermission entity to UserMenuPermissionDto
     * @param permission UserMenuPermission entity
     * @return UserMenuPermissionDto
     */
    public UserMenuPermissionDto toDto(UserMenuPermission permission) {
        if (permission == null) {
            return null;
        }
        
        return UserMenuPermissionDto.builder()
                .id(permission.getId())
                .userId(permission.getUser() != null ? permission.getUser().getId() : null)
                .username(permission.getUser() != null ? permission.getUser().getUsername() : null)
                .menuId(permission.getMenu() != null ? permission.getMenu().getId() : null)
                .menuName(permission.getMenu() != null ? permission.getMenu().getName() : null)
                .moduleId(permission.getMenu() != null && permission.getMenu().getModule() != null ? 
                        permission.getMenu().getModule().getId() : null)
                .moduleName(permission.getMenu() != null && permission.getMenu().getModule() != null ? 
                        permission.getMenu().getModule().getName() : null)
                .moduleKey(permission.getMenu() != null && permission.getMenu().getModule() != null ? 
                        permission.getMenu().getModule().getModuleKey() : null)
                .canView(permission.isCanView())
                .canCreate(permission.isCanCreate())
                .canEdit(permission.isCanEdit())
                .canDelete(permission.isCanDelete())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }

    /**
     * Convert list of UserMenuPermission entities to list of UserMenuPermissionDtos
     * @param permissions List of UserMenuPermission entities
     * @return List of UserMenuPermissionDtos
     */
    public List<UserMenuPermissionDto> toDtoList(List<UserMenuPermission> permissions) {
        if (permissions == null) {
            return List.of();
        }
        
        return permissions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert UserMenuPermissionRequest to UserMenuPermission entity
     * @param request UserMenuPermissionRequest
     * @param user User entity
     * @param menu Menu entity
     * @return UserMenuPermission entity
     */
    public UserMenuPermission toEntity(UserMenuPermissionRequest request, User user, Menu menu) {
        if (request == null) {
            return null;
        }
        
        return UserMenuPermission.builder()
                .user(user)
                .menu(menu)
                .canView(request.isCanView())
                .canCreate(request.isCanCreate())
                .canEdit(request.isCanEdit())
                .canDelete(request.isCanDelete())
                .build();
    }

    /**
     * Update UserMenuPermission entity from UserMenuPermissionRequest
     * @param permission UserMenuPermission entity to update
     * @param request UserMenuPermissionRequest with new values
     * @return Updated UserMenuPermission entity
     */
    public UserMenuPermission updateEntity(UserMenuPermission permission, UserMenuPermissionRequest request) {
        if (permission == null || request == null) {
            return permission;
        }
        
        permission.setCanView(request.isCanView());
        permission.setCanCreate(request.isCanCreate());
        permission.setCanEdit(request.isCanEdit());
        permission.setCanDelete(request.isCanDelete());
        
        return permission;
    }
}