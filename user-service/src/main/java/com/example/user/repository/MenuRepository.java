package com.example.user.repository;

import com.example.user.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {
    
    /**
     * Find menu by name
     * @param name menu name
     * @return Optional of Menu
     */
    Optional<Menu> findByName(String name);
    
    /**
     * Find all menus by module id
     * @param moduleId module id
     * @return List of Menu
     */
    List<Menu> findByModuleId(UUID moduleId);
    
    /**
     * Find all active menus by module id
     * @param moduleId module id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndActiveTrue(UUID moduleId);
    
    /**
     * Find all visible and active menus by module id
     * @param moduleId module id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndActiveTrueAndVisibleTrue(UUID moduleId);
    
    /**
     * Find all menus by router id
     * @param routerId router id
     * @return List of Menu
     */
    List<Menu> findByRouterId(UUID routerId);
    
    /**
     * Find all active menus by router id
     * @param routerId router id
     * @return List of Menu
     */
    List<Menu> findByRouterIdAndActiveTrue(UUID routerId);
    
    /**
     * Find all menus by parent id
     * @param parentId parent menu id
     * @return List of Menu
     */
    List<Menu> findByParentId(UUID parentId);
    
    /**
     * Find all active menus by parent id
     * @param parentId parent menu id
     * @return List of Menu
     */
    List<Menu> findByParentIdAndActiveTrue(UUID parentId);
    
    /**
     * Find all visible and active menus by parent id
     * @param parentId parent menu id
     * @return List of Menu
     */
    List<Menu> findByParentIdAndActiveTrueAndVisibleTrue(UUID parentId);
    
    /**
     * Find all root menus (parentId is null)
     * @return List of Menu
     */
    List<Menu> findByParentIdIsNull();
    
    /**
     * Find all active root menus (parentId is null)
     * @return List of Menu
     */
    List<Menu> findByParentIdIsNullAndActiveTrue();
    
    /**
     * Find all visible and active root menus (parentId is null)
     * @return List of Menu
     */
    List<Menu> findByParentIdIsNullAndActiveTrueAndVisibleTrue();
    
    /**
     * Find all menus by module id and parent id
     * @param moduleId module id
     * @param parentId parent menu id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndParentId(UUID moduleId, UUID parentId);
    
    /**
     * Find all active menus by module id and parent id
     * @param moduleId module id
     * @param parentId parent menu id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndParentIdAndActiveTrue(UUID moduleId, UUID parentId);
    
    /**
     * Find all visible and active menus by module id and parent id
     * @param moduleId module id
     * @param parentId parent menu id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndParentIdAndActiveTrueAndVisibleTrue(UUID moduleId, UUID parentId);
    
    /**
     * Find all root menus by module id (parentId is null)
     * @param moduleId module id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndParentIdIsNull(UUID moduleId);
    
    /**
     * Find all active root menus by module id (parentId is null)
     * @param moduleId module id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndParentIdIsNullAndActiveTrue(UUID moduleId);
    
    /**
     * Find all visible and active root menus by module id (parentId is null)
     * @param moduleId module id
     * @return List of Menu
     */
    List<Menu> findByModuleIdAndParentIdIsNullAndActiveTrueAndVisibleTrue(UUID moduleId);
    
    /**
     * Check if menu exists by name
     * @param name menu name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find all menus by user id through user menu permissions
     * @param userId user id
     * @return List of Menu
     */
    @Query("SELECT DISTINCT m FROM Menu m JOIN m.module mod JOIN UserMenuPermission ump ON ump.menu.id = m.id WHERE ump.user.id = :userId AND ump.canView = true AND m.active = true AND m.visible = true AND mod.isActive = true")
    List<Menu> findAllMenusByUserId(UUID userId);
    
    /**
     * Find all menus by user id and module id through user menu permissions
     * @param userId user id
     * @param moduleId module id
     * @return List of Menu
     */
    @Query("SELECT DISTINCT m FROM Menu m JOIN UserMenuPermission ump ON ump.menu.id = m.id WHERE ump.user.id = :userId AND m.module.id = :moduleId AND ump.canView = true AND m.active = true AND m.visible = true AND m.module.isActive = true")
    List<Menu> findAllMenusByUserIdAndModuleId(UUID userId, UUID moduleId);
}