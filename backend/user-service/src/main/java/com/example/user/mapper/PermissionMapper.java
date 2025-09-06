package com.example.user.mapper;

import com.example.user.dto.PermissionDto;
import com.example.user.dto.request.CreatePermissionRequest;
import com.example.user.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionMapper {

    public PermissionDto toDto(Permission permission) {
        if (permission == null) {
            return null;
        }

        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }

    public List<PermissionDto> toDtoList(List<Permission> permissions) {
        return permissions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Permission toEntity(CreatePermissionRequest request) {
        if (request == null) {
            return null;
        }

        return Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}