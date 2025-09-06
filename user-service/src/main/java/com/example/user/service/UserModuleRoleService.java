package com.example.user.service;

import com.example.user.dto.UserModuleRoleDto;
import com.example.user.dto.UserModuleRoleRequest;
import com.example.user.entity.Module;
import com.example.user.entity.RoleEntity;
import com.example.user.entity.User;
import com.example.user.entity.UserModuleRole;
import com.example.user.exception.ResourceAlreadyExistsException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.UserModuleRoleMapper;
import com.example.user.repository.ModuleRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserModuleRoleRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for UserModuleRole management
 */
@Service
@RequiredArgsConstructor
public class UserModuleRoleService {
    
    private final UserModuleRoleRepository userModuleRoleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final RoleRepository roleRepository;
    private final UserModuleRoleMapper userModuleRoleMapper;
    
    /**
     * Get all user module roles
     * 
     * @return the list of user module roles
     */
    @Transactional(readOnly = true)
    public List<UserModuleRoleDto> getAllUserModuleRoles() {
        List<UserModuleRole> userModuleRoles = userModuleRoleRepository.findAll();
        return userModuleRoleMapper.toDtoList(userModuleRoles);
    }
    
    /**
     * Get user module role by ID
     * 
     * @param id the user module role ID
     * @return the user module role
     * @throws ResourceNotFoundException if the user module role is not found
     */
    @Transactional(readOnly = true)
    public UserModuleRoleDto getUserModuleRoleById(UUID id) {
        UserModuleRole userModuleRole = userModuleRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User module role not found with id: " + id));
        return userModuleRoleMapper.toDto(userModuleRole);
    }
    
    /**
     * Get user module roles by user ID
     * 
     * @param userId the user ID
     * @return the list of user module roles
     */
    @Transactional(readOnly = true)
    public List<UserModuleRoleDto> getUserModuleRolesByUserId(UUID userId) {
        List<UserModuleRole> userModuleRoles = userModuleRoleRepository.findByUserId(userId);
        return userModuleRoleMapper.toDtoList(userModuleRoles);
    }
    
    /**
     * Get user module roles by module ID
     * 
     * @param moduleId the module ID
     * @return the list of user module roles
     */
    @Transactional(readOnly = true)
    public List<UserModuleRoleDto> getUserModuleRolesByModuleId(UUID moduleId) {
        List<UserModuleRole> userModuleRoles = userModuleRoleRepository.findByModuleId(moduleId);
        return userModuleRoleMapper.toDtoList(userModuleRoles);
    }
    
    /**
     * Get user module roles by user ID and module ID
     * 
     * @param userId the user ID
     * @param moduleId the module ID
     * @return the list of user module roles
     */
    @Transactional(readOnly = true)
    public List<UserModuleRoleDto> getUserModuleRolesByUserIdAndModuleId(UUID userId, UUID moduleId) {
        List<UserModuleRole> userModuleRoles = userModuleRoleRepository.findByUserIdAndModuleId(userId, moduleId);
        return userModuleRoleMapper.toDtoList(userModuleRoles);
    }
    
    /**
     * Get user module roles by user ID and module key
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @return the list of user module roles
     */
    @Transactional(readOnly = true)
    public List<UserModuleRoleDto> getUserModuleRolesByUserIdAndModuleKey(UUID userId, String moduleKey) {
        List<UserModuleRole> userModuleRoles = userModuleRoleRepository.findByUserIdAndModuleKey(userId, moduleKey);
        return userModuleRoleMapper.toDtoList(userModuleRoles);
    }
    
    /**
     * Assign a role to a user in a module
     * 
     * @param request the user module role request
     * @return the created user module role
     * @throws ResourceNotFoundException if the user, module or role is not found
     * @throws ResourceAlreadyExistsException if the user already has the role in the module
     */
    @Transactional
    public UserModuleRoleDto assignRoleToUserInModule(UserModuleRoleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));
        
        RoleEntity role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));
        
        // Check if the user already has the role in the module
        if (userModuleRoleRepository.findByUserIdAndModuleIdAndRoleId(
                request.getUserId(), request.getModuleId(), request.getRoleId()).isPresent()) {
            throw new ResourceAlreadyExistsException("User already has the role in the module");
        }
        
        UserModuleRole userModuleRole = userModuleRoleMapper.createEntity(user, module, role);
        userModuleRole = userModuleRoleRepository.save(userModuleRole);
        
        // Add the module role to the user
        user.addModuleRole(userModuleRole);
        userRepository.save(user);
        
        // Add the user module role to the module
        module.addUserModuleRole(userModuleRole);
        moduleRepository.save(module);
        
        return userModuleRoleMapper.toDto(userModuleRole);
    }
    
    /**
     * Remove a role from a user in a module
     * 
     * @param id the user module role ID
     * @throws ResourceNotFoundException if the user module role is not found
     */
    @Transactional
    public void removeRoleFromUserInModule(UUID id) {
        UserModuleRole userModuleRole = userModuleRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User module role not found with id: " + id));
        
        // Remove the module role from the user
        User user = userModuleRole.getUser();
        user.removeModuleRole(userModuleRole);
        userRepository.save(user);
        
        // Remove the user module role from the module
        Module module = userModuleRole.getModule();
        module.removeUserModuleRole(userModuleRole);
        moduleRepository.save(module);
        
        userModuleRoleRepository.delete(userModuleRole);
    }
    
    /**
     * Check if a user has a specific role in a module
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @param roleName the role name
     * @return true if the user has the role in the module, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean hasModuleRole(UUID userId, String moduleKey, String roleName) {
        return userModuleRoleRepository.hasModuleRole(userId, moduleKey, roleName);
    }
    
    /**
     * Check if a user has a specific permission in a module
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @param permissionName the permission name
     * @return true if the user has the permission in the module, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean hasModulePermission(UUID userId, String moduleKey, String permissionName) {
        return userModuleRoleRepository.hasModulePermission(userId, moduleKey, permissionName);
    }
}