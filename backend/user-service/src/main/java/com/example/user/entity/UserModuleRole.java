package com.example.user.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a user's role in a specific module
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_module_roles", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "module_id", "role_id"}))
public class UserModuleRole extends BaseModel {
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
    
    /**
     * Check if the user has a specific permission in this module role
     * 
     * @param permissionName the permission name to check
     * @return true if the user has the permission, false otherwise
     */
    public boolean hasPermission(String permissionName) {
        return role.getPermissions().stream()
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }
}