package com.example.user.repository;

import com.example.user.entity.UserModuleRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for UserModuleRole entity
 */
@Repository
public interface UserModuleRoleRepository extends JpaRepository<UserModuleRole, UUID> {
    
    /**
     * Find user module roles by user ID
     * 
     * @param userId the user ID
     * @return the list of user module roles
     */
    List<UserModuleRole> findByUserId(UUID userId);
    
    /**
     * Find user module roles by module ID
     * 
     * @param moduleId the module ID
     * @return the list of user module roles
     */
    List<UserModuleRole> findByModuleId(UUID moduleId);
    
    /**
     * Find user module roles by role ID
     * 
     * @param roleId the role ID
     * @return the list of user module roles
     */
    List<UserModuleRole> findByRoleId(UUID roleId);
    
    /**
     * Find user module role by user ID, module ID and role ID
     * 
     * @param userId the user ID
     * @param moduleId the module ID
     * @param roleId the role ID
     * @return the user module role if found
     */
    Optional<UserModuleRole> findByUserIdAndModuleIdAndRoleId(UUID userId, UUID moduleId, UUID roleId);
    
    /**
     * Find user module roles by user ID and module ID
     * 
     * @param userId the user ID
     * @param moduleId the module ID
     * @return the list of user module roles
     */
    List<UserModuleRole> findByUserIdAndModuleId(UUID userId, UUID moduleId);
    
    /**
     * Find user module roles by user ID and module key
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @return the list of user module roles
     */
    @Query("SELECT umr FROM UserModuleRole umr WHERE umr.user.id = :userId AND umr.module.moduleKey = :moduleKey")
    List<UserModuleRole> findByUserIdAndModuleKey(@Param("userId") UUID userId, @Param("moduleKey") String moduleKey);
    
    /**
     * Check if a user has a specific role in a module
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @param roleName the role name
     * @return true if the user has the role in the module, false otherwise
     */
    @Query("SELECT COUNT(umr) > 0 FROM UserModuleRole umr WHERE umr.user.id = :userId AND umr.module.moduleKey = :moduleKey AND umr.role.name = :roleName")
    boolean hasModuleRole(@Param("userId") UUID userId, @Param("moduleKey") String moduleKey, @Param("roleName") String roleName);
    
    /**
     * Check if a user has a specific permission in a module
     * 
     * @param userId the user ID
     * @param moduleKey the module key
     * @param permissionName the permission name
     * @return true if the user has the permission in the module, false otherwise
     */
    @Query("SELECT COUNT(umr) > 0 FROM UserModuleRole umr JOIN umr.role.permissions p WHERE umr.user.id = :userId AND umr.module.moduleKey = :moduleKey AND p.name = :permissionName")
    boolean hasModulePermission(@Param("userId") UUID userId, @Param("moduleKey") String moduleKey, @Param("permissionName") String permissionName);
}