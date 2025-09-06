package com.example.user.mapper;

import com.example.user.dto.RoleDto;
import com.example.user.dto.request.CreateRoleRequest;
import com.example.user.dto.request.UpdateRoleRequest;
import com.example.user.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    @Autowired
    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public RoleDto toDto(RoleEntity role) {
        if (role == null) {
            return null;
        }

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getPermissions() != null ?
                        role.getPermissions().stream()
                                .map(permissionMapper::toDto)
                                .collect(Collectors.toSet()) :
                        null)
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    public List<RoleDto> toDtoList(List<RoleEntity> roles) {
        return roles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RoleEntity toEntity(CreateRoleRequest request) {
        if (request == null) {
            return null;
        }

        return RoleEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public void updateEntityFromRequest(RoleEntity role, UpdateRoleRequest request) {
        if (request.getName() != null) {
            role.setName(request.getName());
        }
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
    }
}