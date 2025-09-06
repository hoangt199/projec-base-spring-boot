package com.example.user.repository;

import com.example.user.entity.Router;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouterRepository extends JpaRepository<Router, UUID> {
    
    /**
     * Find router by path
     * @param path router path
     * @return Optional of Router
     */
    Optional<Router> findByPath(String path);
    
    /**
     * Find router by name
     * @param name router name
     * @return Optional of Router
     */
    Optional<Router> findByName(String name);
    
    /**
     * Find all routers by module id
     * @param moduleId module id
     * @return List of Router
     */
    List<Router> findByModuleId(UUID moduleId);
    
    /**
     * Find all active routers by module id
     * @param moduleId module id
     * @return List of Router
     */
    List<Router> findByModuleIdAndActiveTrue(UUID moduleId);
    
    /**
     * Find all routers by parent id
     * @param parentId parent router id
     * @return List of Router
     */
    List<Router> findByParentId(UUID parentId);
    
    /**
     * Find all active routers by parent id
     * @param parentId parent router id
     * @return List of Router
     */
    List<Router> findByParentIdAndActiveTrue(UUID parentId);
    
    /**
     * Find all root routers (parentId is null)
     * @return List of Router
     */
    List<Router> findByParentIdIsNull();
    
    /**
     * Find all active root routers (parentId is null)
     * @return List of Router
     */
    List<Router> findByParentIdIsNullAndActiveTrue();
    
    /**
     * Find all routers by module id and parent id
     * @param moduleId module id
     * @param parentId parent router id
     * @return List of Router
     */
    List<Router> findByModuleIdAndParentId(UUID moduleId, UUID parentId);
    
    /**
     * Find all active routers by module id and parent id
     * @param moduleId module id
     * @param parentId parent router id
     * @return List of Router
     */
    List<Router> findByModuleIdAndParentIdAndActiveTrue(UUID moduleId, UUID parentId);
    
    /**
     * Find all root routers by module id (parentId is null)
     * @param moduleId module id
     * @return List of Router
     */
    List<Router> findByModuleIdAndParentIdIsNull(UUID moduleId);
    
    /**
     * Find all active root routers by module id (parentId is null)
     * @param moduleId module id
     * @return List of Router
     */
    List<Router> findByModuleIdAndParentIdIsNullAndActiveTrue(UUID moduleId);
    
    /**
     * Check if router exists by path
     * @param path router path
     * @return true if exists, false otherwise
     */
    boolean existsByPath(String path);
    
    /**
     * Check if router exists by name
     * @param name router name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find all routers by user id through user module roles
     * @param userId user id
     * @return List of Router
     */
    @Query("SELECT DISTINCT r FROM Router r JOIN r.module m JOIN m.userModuleRoles umr WHERE umr.user.id = :userId AND r.active = true AND m.isActive = true")
    List<Router> findAllRoutersByUserId(UUID userId);
}