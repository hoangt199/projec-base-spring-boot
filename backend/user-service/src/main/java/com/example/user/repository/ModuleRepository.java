package com.example.user.repository;

import com.example.user.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Module entity
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
    
    /**
     * Find a module by its key
     * 
     * @param moduleKey the module key
     * @return the module if found
     */
    Optional<Module> findByModuleKey(String moduleKey);
    
    /**
     * Find a module by its name
     * 
     * @param name the module name
     * @return the module if found
     */
    Optional<Module> findByName(String name);
    
    /**
     * Find all active modules
     * 
     * @return the list of active modules
     */
    List<Module> findByIsActiveTrue();
    
    /**
     * Check if a module exists by its key
     * 
     * @param moduleKey the module key
     * @return true if the module exists, false otherwise
     */
    boolean existsByModuleKey(String moduleKey);
    
    /**
     * Find modules by user ID
     * 
     * @param userId the user ID
     * @return the list of modules
     */
    @Query("SELECT DISTINCT umr.module FROM UserModuleRole umr WHERE umr.user.id = :userId")
    List<Module> findByUserId(@Param("userId") UUID userId);
}