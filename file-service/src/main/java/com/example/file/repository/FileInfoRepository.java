package com.example.file.repository;

import com.example.file.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for file information
 */
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {

    /**
     * Find files by owner ID
     *
     * @param ownerId the owner ID
     * @param pageable pagination information
     * @return page of files
     */
    Page<FileInfo> findByOwnerIdAndIsDeletedFalse(UUID ownerId, Pageable pageable);
    
    /**
     * Find files by owner ID and parent folder ID
     *
     * @param ownerId the owner ID
     * @param parentFolderId the parent folder ID
     * @param pageable pagination information
     * @return page of files
     */
    Page<FileInfo> findByOwnerIdAndParentFolderIdAndIsDeletedFalse(UUID ownerId, UUID parentFolderId, Pageable pageable);
    
    /**
     * Find files by group ID
     *
     * @param groupId the group ID
     * @param pageable pagination information
     * @return page of files
     */
    Page<FileInfo> findByGroupIdAndIsDeletedFalse(UUID groupId, Pageable pageable);
    
    /**
     * Find public files
     *
     * @param pageable pagination information
     * @return page of files
     */
    Page<FileInfo> findByIsPublicTrueAndIsDeletedFalse(Pageable pageable);
    
    /**
     * Find file by ID and owner ID
     *
     * @param id the file ID
     * @param ownerId the owner ID
     * @return the file if found
     */
    Optional<FileInfo> findByIdAndOwnerIdAndIsDeletedFalse(UUID id, UUID ownerId);
    
    /**
     * Find file by ID and group ID
     *
     * @param id the file ID
     * @param groupId the group ID
     * @return the file if found
     */
    Optional<FileInfo> findByIdAndGroupIdAndIsDeletedFalse(UUID id, UUID groupId);
    
    /**
     * Find public file by ID
     *
     * @param id the file ID
     * @return the file if found
     */
    Optional<FileInfo> findByIdAndIsPublicTrueAndIsDeletedFalse(UUID id);
    
    /**
     * Find all files in a folder
     *
     * @param parentFolderId the parent folder ID
     * @return list of files
     */
    List<FileInfo> findByParentFolderIdAndIsDeletedFalse(UUID parentFolderId);
    
    /**
     * Search files by name and owner ID
     *
     * @param name the file name to search for
     * @param ownerId the owner ID
     * @param pageable pagination information
     * @return page of files
     */
    @Query("SELECT f FROM FileInfo f WHERE f.originalName LIKE %:name% AND f.ownerId = :ownerId AND f.isDeleted = false")
    Page<FileInfo> searchByNameAndOwnerId(@Param("name") String name, @Param("ownerId") UUID ownerId, Pageable pageable);
}