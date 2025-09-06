package com.example.file.repository;

import com.example.file.entity.FilePermission;
import com.example.file.entity.FilePermission.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for file permissions
 */
@Repository
public interface FilePermissionRepository extends JpaRepository<FilePermission, UUID> {

    /**
     * Find permission by file ID and user ID
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @return the permission if found
     */
    Optional<FilePermission> findByFileIdAndUserId(UUID fileId, UUID userId);
    
    /**
     * Find permissions by file ID
     *
     * @param fileId the file ID
     * @return list of permissions
     */
    List<FilePermission> findByFileId(UUID fileId);
    
    /**
     * Find permissions by user ID
     *
     * @param userId the user ID
     * @return list of permissions
     */
    List<FilePermission> findByUserId(UUID userId);
    
    /**
     * Find permissions by file ID and permission type
     *
     * @param fileId the file ID
     * @param permissionType the permission type
     * @return list of permissions
     */
    List<FilePermission> findByFileIdAndPermissionType(UUID fileId, PermissionType permissionType);
    
    /**
     * Check if user has permission for a file
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @param permissionType the permission type
     * @return true if user has permission
     */
    @Query("SELECT COUNT(p) > 0 FROM FilePermission p WHERE p.file.id = :fileId AND p.userId = :userId AND p.permissionType = :permissionType AND p.isDeleted = false")
    boolean hasPermission(@Param("fileId") UUID fileId, @Param("userId") UUID userId, @Param("permissionType") PermissionType permissionType);
    
    /**
     * Delete permissions by file ID
     *
     * @param fileId the file ID
     */
    void deleteByFileId(UUID fileId);
}