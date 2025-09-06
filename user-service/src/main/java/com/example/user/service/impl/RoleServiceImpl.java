package com.example.user.service.impl;

import com.example.user.dto.RoleDto;
import com.example.user.dto.request.AssignPermissionsRequest;
import com.example.user.dto.request.CreateRoleRequest;
import com.example.user.dto.request.UpdateRoleRequest;
import com.example.user.entity.Permission;
import com.example.user.entity.RoleEntity;
import com.example.user.exception.ResourceAlreadyExistsException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.RoleMapper;
import com.example.user.repository.PermissionRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDto> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        return roleMapper.toDtoList(roles);
    }

    @Override
    public RoleDto getRoleById(UUID id) {
        RoleEntity role = findRoleById(id);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional
    public RoleDto createRole(CreateRoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Role with name " + request.getName() + " already exists");
        }

        RoleEntity role = roleMapper.toEntity(request);
        RoleEntity savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    @Transactional
    public RoleDto updateRole(UUID id, UpdateRoleRequest request) {
        RoleEntity role = findRoleById(id);

        // Check if name is being changed and if it already exists
        if (request.getName() != null && !request.getName().equals(role.getName()) 
                && roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Role with name " + request.getName() + " already exists");
        }

        roleMapper.updateEntityFromRequest(role, request);
        RoleEntity updatedRole = roleRepository.save(role);
        return roleMapper.toDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDto assignPermissionsToRole(UUID roleId, AssignPermissionsRequest request) {
        RoleEntity role = findRoleById(roleId);
        Set<Permission> permissions = permissionRepository.findByIdIn(request.getPermissionIds());

        if (permissions.size() != request.getPermissionIds().size()) {
            throw new ResourceNotFoundException("One or more permissions not found");
        }

        role.setPermissions(permissions);
        RoleEntity updatedRole = roleRepository.save(role);
        return roleMapper.toDto(updatedRole);
    }

    private RoleEntity findRoleById(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }
}