package com.example.user.repository;

import com.example.user.entity.UserMenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserMenuPermissionRepository extends JpaRepository<UserMenuPermission, UUID> {
    
    /**
     * Find all permissions by user id
     * @param userId user id
     * @return List of UserMenuPermission
     */
    List<UserMenuPermission> findByUserId(UUID userId);
    
    /**
     * Find all permissions by menu id
     * @param menuId menu id
     * @return List of UserMenuPermission
     */
    List<UserMenuPermission> findByMenuId(UUID menuId);
    
    /**
     * Find permission by user id and menu id
     * @param userId user id
     * @param menuId menu id
     * @return Optional of UserMenuPermission
     */
    Optional<UserMenuPermission> findByUserIdAndMenuId(UUID userId, UUID menuId);
    
    /**
     * Find all permissions by user id and module id
     * @param userId user id
     * @param moduleId module id
     * @return List of UserMenuPermission
     */
    @Query("SELECT ump FROM UserMenuPermission ump WHERE ump.user.id = :userId AND ump.menu.module.id = :moduleId")
    List<UserMenuPermission> findByUserIdAndModuleId(UUID userId, UUID moduleId);
    
    /**
     * Check if user has view permission for a menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    boolean existsByUserIdAndMenuIdAndCanViewTrue(UUID userId, UUID menuId);
    
    /**
     * Check if user has permission for a menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    boolean existsByUserIdAndMenuId(UUID userId, UUID menuId);
    
    /**
     * Check if user has create permission for a menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    boolean existsByUserIdAndMenuIdAndCanCreateTrue(UUID userId, UUID menuId);
    
    /**
     * Check if user has edit permission for a menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    boolean existsByUserIdAndMenuIdAndCanEditTrue(UUID userId, UUID menuId);
    
    /**
     * Check if user has delete permission for a menu
     * @param userId user id
     * @param menuId menu id
     * @return true if has permission, false otherwise
     */
    boolean existsByUserIdAndMenuIdAndCanDeleteTrue(UUID userId, UUID menuId);
    
    /**
     * Delete all permissions by user id
     * @param userId user id
     */
    void deleteByUserId(UUID userId);
    
    /**
     * Delete all permissions by menu id
     * @param menuId menu id
     */
    void deleteByMenuId(UUID menuId);
    
    /**
     * Delete permission by user id and menu id
     * @param userId user id
     * @param menuId menu id
     */
    void deleteByUserIdAndMenuId(UUID userId, UUID menuId);
}