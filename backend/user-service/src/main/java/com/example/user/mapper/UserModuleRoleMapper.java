package com.example.user.mapper;

import com.example.user.dto.UserModuleRoleDto;
import com.example.user.dto.UserModuleRoleRequest;
import com.example.user.entity.Module;
import com.example.user.entity.RoleEntity;
import com.example.user.entity.User;
import com.example.user.entity.UserModuleRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

/**
 * Mapper for UserModuleRole entity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserModuleRoleMapper {
    
    /**
     * Convert UUID to Long
     * @param value UUID value
     * @return Long value
     */
    default Long map(UUID value) {
        if (value == null) {
            return null;
        }
        return Long.valueOf(value.getMostSignificantBits());
    }
    
    /**
     * Convert UserModuleRole entity to UserModuleRoleDto
     * 
     * @param userModuleRole the UserModuleRole entity
     * @return the UserModuleRoleDto
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(source = "module.name", target = "moduleName")
    @Mapping(source = "module.moduleKey", target = "moduleKey")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    UserModuleRoleDto toDto(UserModuleRole userModuleRole);
    
    /**
     * Convert list of UserModuleRole entities to list of UserModuleRoleDtos
     * 
     * @param userModuleRoles the list of UserModuleRole entities
     * @return the list of UserModuleRoleDtos
     */
    List<UserModuleRoleDto> toDtoList(List<UserModuleRole> userModuleRoles);
    
    /**
     * Create UserModuleRole entity from User, Module and RoleEntity
     * 
     * @param user the User entity
     * @param module the Module entity
     * @param role the RoleEntity
     * @return the UserModuleRole entity
     */
    default UserModuleRole createEntity(User user, Module module, RoleEntity role) {
        if (user == null || module == null || role == null) {
            return null;
        }
        
        UserModuleRole userModuleRole = new UserModuleRole();
        userModuleRole.setUser(user);
        userModuleRole.setModule(module);
        userModuleRole.setRole(role);
        
        return userModuleRole;
    }
}