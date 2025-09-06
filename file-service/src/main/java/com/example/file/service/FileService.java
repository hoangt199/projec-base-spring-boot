package com.example.file.service;

import com.example.common.dto.PageDto;
import com.example.common.exception.BusinessException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.file.client.NotificationServiceClient;
import com.example.file.client.UserServiceClient;
import com.example.file.dto.FileInfoDto;
import com.example.file.entity.FileInfo;
import com.example.file.entity.FilePermission;
import com.example.file.entity.FilePermission.PermissionType;
import com.example.file.mapper.FileInfoMapper;
import com.example.file.repository.FileInfoRepository;
import com.example.file.repository.FilePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for file management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileInfoRepository fileInfoRepository;
    private final FilePermissionRepository filePermissionRepository;
    private final MinioService minioService;
    private final FileInfoMapper fileInfoMapper;
    private final UserServiceClient userServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    /**
     * Upload a file
     *
     * @param file the file to upload
     * @param ownerId the owner ID
     * @param parentFolderId the parent folder ID (optional)
     * @param isPublic whether the file is public
     * @param groupId the group ID (optional)
     * @return the file information
     */
    @Transactional
    public FileInfoDto uploadFile(MultipartFile file, UUID ownerId, UUID parentFolderId, boolean isPublic, UUID groupId) {
        try {
            // Generate a unique object name for MinIO
            String objectName = generateObjectName(file.getOriginalFilename());
            
            // Upload file to MinIO
            minioService.uploadFile(file, objectName);
            
            // Extract file extension
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            }
            
            // Extract media type (first part of content type)
            String contentType = file.getContentType();
            String mediaType = "";
            if (contentType != null && contentType.contains("/")) {
                mediaType = contentType.substring(0, contentType.indexOf("/"));
            }
            
            // Create file info in database
            FileInfo fileInfo = FileInfo.builder()
                    .originalName(originalFilename)
                    .filePath(objectName)
                    .fileSize(file.getSize())
                    .contentType(contentType)
                    .mediaType(mediaType)
                    .extension(extension)
                    .isFolder(false)
                    .parentFolderId(parentFolderId)
                    .ownerId(ownerId)
                    .groupId(groupId)
                    .isPublic(isPublic)
                    .build();
            
            FileInfo savedFileInfo = fileInfoRepository.save(fileInfo);
            return fileInfoMapper.toDto(savedFileInfo);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new BusinessException("Error uploading file: " + e.getMessage());
        }
    }

    /**
     * Create a folder
     *
     * @param folderName the folder name
     * @param ownerId the owner ID
     * @param parentFolderId the parent folder ID (optional)
     * @param isPublic whether the folder is public
     * @param groupId the group ID (optional)
     * @return the folder information
     */
    @Transactional
    public FileInfoDto createFolder(String folderName, UUID ownerId, UUID parentFolderId, boolean isPublic, UUID groupId) {
        // Create folder info in database
        FileInfo folderInfo = FileInfo.builder()
                .originalName(folderName)
                .filePath(generateFolderPath(folderName))
                .fileSize(0L)
                .contentType("folder")
                .isFolder(true)
                .parentFolderId(parentFolderId)
                .ownerId(ownerId)
                .groupId(groupId)
                .isPublic(isPublic)
                .build();
        
        FileInfo savedFolderInfo = fileInfoRepository.save(folderInfo);
        return fileInfoMapper.toDto(savedFolderInfo);
    }

    /**
     * Download a file
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @return the file input stream
     */
    public InputStream downloadFile(UUID fileId, UUID userId) {
        FileInfo fileInfo = findFileWithAccessCheck(fileId, userId);
        
        if (fileInfo.isFolder()) {
            throw new BusinessException("Cannot download a folder");
        }
        
        return minioService.downloadFile(fileInfo.getFilePath());
    }

    /**
     * Delete a file or folder
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @return true if deleted successfully
     */
    @Transactional
    public boolean deleteFile(UUID fileId, UUID userId) {
        FileInfo fileInfo = findFileWithAccessCheck(fileId, userId);
        
        if (fileInfo.isFolder()) {
            // Delete all files in the folder
            List<FileInfo> filesInFolder = fileInfoRepository.findByParentFolderIdAndIsDeletedFalse(fileId);
            for (FileInfo file : filesInFolder) {
                deleteFile(file.getId(), userId);
            }
        } else {
            // Delete file from MinIO
            minioService.deleteFile(fileInfo.getFilePath());
        }
        
        // Soft delete in database
        fileInfo.setDeleted(true);
        fileInfo.setUpdatedAt(LocalDateTime.now());
        fileInfoRepository.save(fileInfo);
        
        return true;
    }

    /**
     * Get files by owner ID
     *
     * @param ownerId the owner ID
     * @param parentFolderId the parent folder ID (optional)
     * @param page the page number
     * @param size the page size
     * @return page of files
     */
    public PageDto<FileInfoDto> getFilesByOwner(UUID ownerId, UUID parentFolderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FileInfo> filePage;
        
        if (parentFolderId != null) {
            filePage = fileInfoRepository.findByOwnerIdAndParentFolderIdAndIsDeletedFalse(ownerId, parentFolderId, pageable);
        } else {
            filePage = fileInfoRepository.findByOwnerIdAndIsDeletedFalse(ownerId, pageable);
        }
        
        List<FileInfoDto> fileDtos = filePage.getContent().stream()
                .map(fileInfoMapper::toDto)
                .collect(Collectors.toList());
        
        return PageDto.of(fileDtos, filePage.getTotalElements(), filePage.getTotalPages(), page, size);
    }

    /**
     * Get files by group ID
     *
     * @param groupId the group ID
     * @param page the page number
     * @param size the page size
     * @return page of files
     */
    public PageDto<FileInfoDto> getFilesByGroup(UUID groupId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FileInfo> filePage = fileInfoRepository.findByGroupIdAndIsDeletedFalse(groupId, pageable);
        
        List<FileInfoDto> fileDtos = filePage.getContent().stream()
                .map(fileInfoMapper::toDto)
                .collect(Collectors.toList());
        
        return PageDto.of(fileDtos, filePage.getTotalElements(), filePage.getTotalPages(), page, size);
    }

    /**
     * Get public files
     *
     * @param page the page number
     * @param size the page size
     * @return page of files
     */
    public PageDto<FileInfoDto> getPublicFiles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FileInfo> filePage = fileInfoRepository.findByIsPublicTrueAndIsDeletedFalse(pageable);
        
        List<FileInfoDto> fileDtos = filePage.getContent().stream()
                .map(fileInfoMapper::toDto)
                .collect(Collectors.toList());
        
        return PageDto.of(fileDtos, filePage.getTotalElements(), filePage.getTotalPages(), page, size);
    }

    /**
     * Search files by name
     *
     * @param name the name to search for
     * @param ownerId the owner ID
     * @param page the page number
     * @param size the page size
     * @return page of files
     */
    public PageDto<FileInfoDto> searchFilesByName(String name, UUID ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FileInfo> filePage = fileInfoRepository.searchByNameAndOwnerId(name, ownerId, pageable);
        
        List<FileInfoDto> fileDtos = filePage.getContent().stream()
                .map(fileInfoMapper::toDto)
                .collect(Collectors.toList());
        
        return PageDto.of(fileDtos, filePage.getTotalElements(), filePage.getTotalPages(), page, size);
    }

    /**
     * Get file information
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @return the file information
     */
    public FileInfoDto getFileInfo(UUID fileId, UUID userId) {
        FileInfo fileInfo = findFileWithAccessCheck(fileId, userId);
        return fileInfoMapper.toDto(fileInfo);
    }

    /**
     * Update file access permissions
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @param isPublic whether the file is public
     * @param groupId the group ID (optional)
     * @param accessPermissions the access permissions
     * @return the updated file information
     */
    @Transactional
    public FileInfoDto updateFileAccess(UUID fileId, UUID userId, boolean isPublic, UUID groupId, String accessPermissions) {
        FileInfo fileInfo = findFileWithOwnerCheck(fileId, userId);
        
        fileInfo.setPublic(isPublic);
        fileInfo.setGroupId(groupId);
        fileInfo.setAccessPermissions(accessPermissions);
        fileInfo.setUpdatedAt(LocalDateTime.now());
        
        FileInfo updatedFileInfo = fileInfoRepository.save(fileInfo);
        return fileInfoMapper.toDto(updatedFileInfo);
    }
    
    /**
     * Share file with a user
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @param permissionType the permission type
     * @return the file permission
     */
    @Transactional
    public FilePermission shareFile(UUID fileId, UUID userId, PermissionType permissionType) {
        // Check if file exists
        FileInfo fileInfo = fileInfoRepository.findByIdAndIsDeletedFalse(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId.toString()));
        
        // Check if user exists
        if (!userServiceClient.userExists(userId)) {
            throw new BusinessException("User not found with ID: " + userId);
        }
        
        // Check if permission already exists
        Optional<FilePermission> existingPermission = filePermissionRepository.findByFileIdAndUserId(fileId, userId);
        
        FilePermission permission;
        if (existingPermission.isPresent()) {
            // Update existing permission
            permission = existingPermission.get();
            permission.setPermissionType(permissionType);
        } else {
            // Create new permission
            permission = FilePermission.builder()
                    .file(fileInfo)
                    .userId(userId)
                    .permissionType(permissionType)
                    .build();
            
            fileInfo.addPermission(permission);
        }
        
        permission = filePermissionRepository.save(permission);
        
        // Send notification to user
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("type", "FILE_SHARED");
            notification.put("userId", userId);
            notification.put("fileId", fileId);
            notification.put("fileName", fileInfo.getOriginalName());
            notification.put("sharedBy", fileInfo.getOwnerId());
            notification.put("permissionType", permissionType.name());
            
            notificationServiceClient.sendNotification(notification);
        } catch (Exception e) {
            log.error("Failed to send notification for file sharing: {}", e.getMessage());
            // Continue execution even if notification fails
        }
        
        return permission;
    }
    
    /**
     * Remove file sharing for a user
     *
     * @param fileId the file ID
     * @param userId the user ID
     */
    @Transactional
    public void removeFileSharing(UUID fileId, UUID userId) {
        // Check if file exists
        FileInfo fileInfo = fileInfoRepository.findByIdAndIsDeletedFalse(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId.toString()));
        
        // Check if permission exists
        FilePermission permission = filePermissionRepository.findByFileIdAndUserId(fileId, userId)
                .orElseThrow(() -> new BusinessException("Permission not found for user ID: " + userId));
        
        // Remove permission
        fileInfo.removePermission(permission);
        filePermissionRepository.delete(permission);
        
        // Send notification to user
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("type", "FILE_UNSHARED");
            notification.put("userId", userId);
            notification.put("fileId", fileId);
            notification.put("fileName", fileInfo.getOriginalName());
            notification.put("unsharedBy", fileInfo.getOwnerId());
            
            notificationServiceClient.sendNotification(notification);
        } catch (Exception e) {
            log.error("Failed to send notification for file unsharing: {}", e.getMessage());
            // Continue execution even if notification fails
        }
    }
    
    /**
     * Get file permissions
     *
     * @param fileId the file ID
     * @return the list of permissions
     */
    public List<FilePermission> getFilePermissions(UUID fileId) {
        // Check if file exists
        if (!fileInfoRepository.existsByIdAndIsDeletedFalse(fileId)) {
            throw new ResourceNotFoundException("File", "id", fileId.toString());
        }
        
        return filePermissionRepository.findByFileId(fileId);
    }
    
    /**
     * Check if user has permission for a file
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @param permissionType the permission type
     * @return true if user has permission
     */
    public boolean hasPermission(UUID fileId, UUID userId, PermissionType permissionType) {
        // Owner always has all permissions
        FileInfo fileInfo = fileInfoRepository.findByIdAndIsDeletedFalse(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId.toString()));
        
        if (fileInfo.getOwnerId().equals(userId)) {
            return true;
        }
        
        // Public files have READ permission for everyone
        if (fileInfo.isPublic() && permissionType == PermissionType.READ) {
            return true;
        }
        
        // Check specific permission
        return filePermissionRepository.hasPermission(fileId, userId, permissionType);
    }

    /**
     * Find a file with access check
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @return the file information
     */
    private FileInfo findFileWithAccessCheck(UUID fileId, UUID userId) {
        // Check if user is the owner
        FileInfo fileInfo = fileInfoRepository.findByIdAndOwnerIdAndIsDeletedFalse(fileId, userId)
                .orElse(null);
        
        if (fileInfo == null) {
            // Check if file is in user's group
            fileInfo = fileInfoRepository.findById(fileId)
                    .filter(f -> !f.isDeleted())
                    .filter(f -> f.getGroupId() != null && userHasGroupAccess(userId, f.getGroupId()))
                    .orElse(null);
        }
        
        if (fileInfo == null) {
            // Check if file is public
            fileInfo = fileInfoRepository.findByIdAndIsPublicTrueAndIsDeletedFalse(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId.toString()));
        }
        
        return fileInfo;
    }

    /**
     * Find a file with owner check
     *
     * @param fileId the file ID
     * @param ownerId the owner ID
     * @return the file information
     */
    private FileInfo findFileWithOwnerCheck(UUID fileId, UUID ownerId) {
        return fileInfoRepository.findByIdAndOwnerIdAndIsDeletedFalse(fileId, ownerId)
                .orElseThrow(() -> new BusinessException("You don't have permission to modify this file"));
    }

    /**
     * Check if a user has access to a group
     *
     * @param userId the user ID
     * @param groupId the group ID
     * @return true if the user has access
     */
    private boolean userHasGroupAccess(UUID userId, UUID groupId) {
        // TODO: Implement group access check
        // This would typically involve checking if the user is a member of the group
        return false;
    }

    /**
     * Generate a unique object name for MinIO
     *
     * @param originalFilename the original filename
     * @return the object name
     */
    private String generateObjectName(String originalFilename) {
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * Generate a folder path
     *
     * @param folderName the folder name
     * @return the folder path
     */
    private String generateFolderPath(String folderName) {
        return "folders/" + UUID.randomUUID().toString() + "/";
    }
}