package com.example.user.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entity representing a system module
 * Examples: File Management, User Management, etc.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modules")
public class Module extends BaseModel {
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false, unique = true, name = "module_key")
    private String moduleKey;
    
    @Column(nullable = false)
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserModuleRole> userModuleRoles = new HashSet<>();
    
    /**
     * Add a user module role
     * 
     * @param userModuleRole the user module role to add
     * @return the module
     */
    public Module addUserModuleRole(UserModuleRole userModuleRole) {
        userModuleRoles.add(userModuleRole);
        userModuleRole.setModule(this);
        return this;
    }
    
    /**
     * Remove a user module role
     * 
     * @param userModuleRole the user module role to remove
     * @return the module
     */
    public Module removeUserModuleRole(UserModuleRole userModuleRole) {
        userModuleRoles.remove(userModuleRole);
        userModuleRole.setModule(null);
        return this;
    }
}