package com.example.file.controller;

import com.example.common.dto.ApiResponse;
import com.example.common.dto.PageDto;
import com.example.file.dto.FileInfoDto;
import com.example.file.dto.FilePermissionDto;
import com.example.file.dto.ShareFileRequest;
import com.example.file.entity.FilePermission;
import com.example.file.entity.FilePermission.PermissionType;
import com.example.file.mapper.FilePermissionMapper;
import com.example.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Controller for file management
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Management", description = "APIs for file management")
public class FileController {

    private final FileService fileService;
    private final FilePermissionMapper filePermissionMapper;

    /**
     * Upload file
     *
     * @param file the file to upload
     * @param parentFolderId the parent folder ID (optional)
     * @param isPublic whether the file is public (optional)
     * @param groupId the group ID (optional)
     * @return the uploaded file info
     */
    @PostMapping("/files/upload")
    @Operation(summary = "Upload file", description = "Upload a file to the server")
    public ResponseEntity<ApiResponse<FileInfoDto>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "parentFolderId", required = false) UUID parentFolderId,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
            @RequestParam(value = "groupId", required = false) UUID groupId) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        FileInfoDto fileInfoDto = fileService.uploadFile(file, parentFolderId, currentUserId, groupId, isPublic);
        return ResponseEntity.ok(ApiResponse.success(fileInfoDto));
    }
    
    /**
     * Create folder
     *
     * @param folderName the folder name
     * @param parentFolderId the parent folder ID (optional)
     * @param isPublic whether the folder is public (optional)
     * @param groupId the group ID (optional)
     * @return the created folder info
     */
    @PostMapping("/folders")
    @Operation(summary = "Create folder", description = "Create a new folder")
    public ResponseEntity<ApiResponse<FileInfoDto>> createFolder(
            @RequestParam("folderName") String folderName,
            @RequestParam(value = "parentFolderId", required = false) UUID parentFolderId,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
            @RequestParam(value = "groupId", required = false) UUID groupId) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        FileInfoDto folderDto = fileService.createFolder(folderName, parentFolderId, currentUserId, groupId, isPublic);
        return ResponseEntity.ok(ApiResponse.success(folderDto));
    }
    
    /**
     * Download file
     *
     * @param fileId the file ID
     * @param response the HTTP response
     * @return the file content
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/files/{fileId}/download")
    @Operation(summary = "Download file", description = "Download a file by ID")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable UUID fileId,
            HttpServletResponse response) throws IOException {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Get file info
        FileInfoDto fileInfo = fileService.getFileInfo(fileId);
        
        // Check if file is a folder
        if (fileInfo.isFolder()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Check if user has permission to download
        if (!fileInfo.isPublic() && !fileService.hasPermission(fileId, currentUserId, PermissionType.READ)) {
            return ResponseEntity.status(403).build();
        }
        
        // Download file
        InputStream inputStream = fileService.downloadFile(fileId);
        
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileInfo.getOriginalName());
        
        // Set content type if available
        if (fileInfo.getContentType() != null && !fileInfo.getContentType().isEmpty()) {
            headers.setContentType(MediaType.parseMediaType(fileInfo.getContentType()));
        } else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(inputStream));
    }
    
    /**
     * Delete file or folder
     *
     * @param fileId the file ID
     * @return the response
     */
    @DeleteMapping("/files/{fileId}")
    @Operation(summary = "Delete file or folder", description = "Delete a file or folder by ID (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable UUID fileId) {
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to delete
        if (!fileService.hasPermission(fileId, currentUserId, PermissionType.OWNER)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to delete this file"));
        }
        
        fileService.deleteFile(fileId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    /**
     * Restore file or folder from trash
     *
     * @param fileId the file ID
     * @return the restored file info
     */
    @PutMapping("/files/{fileId}/restore")
    @Operation(summary = "Restore file or folder", description = "Restore a file or folder from trash")
    public ResponseEntity<ApiResponse<FileInfoDto>> restoreFile(@PathVariable UUID fileId) {
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to restore
        if (!fileService.hasPermission(fileId, currentUserId, PermissionType.OWNER)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to restore this file"));
        }
        
        FileInfoDto fileInfo = fileService.restoreFile(fileId);
        return ResponseEntity.ok(ApiResponse.success(fileInfo));
    }
    
    /**
     * Get file info
     *
     * @param fileId the file ID
     * @return the file info
     */
    @GetMapping("/files/{fileId}")
    @Operation(summary = "Get file info", description = "Get file information by ID")
    public ResponseEntity<ApiResponse<FileInfoDto>> getFileInfo(@PathVariable UUID fileId) {
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Get file info
        FileInfoDto fileInfo = fileService.getFileInfo(fileId);
        
        // Check if user has permission to view
        if (!fileInfo.isPublic() && !fileService.hasPermission(fileId, currentUserId, PermissionType.READ)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to view this file"));
        }
        
        return ResponseEntity.ok(ApiResponse.success(fileInfo));
    }
    
    /**
     * Get files by owner
     *
     * @param ownerId the owner ID
     * @param page the page number
     * @param size the page size
     * @return the files
     */
    @GetMapping("/files/owner/{ownerId}")
    @Operation(summary = "Get files by owner", description = "Get files owned by a specific user")
    public ResponseEntity<ApiResponse<PageDto<FileInfoDto>>> getFilesByOwner(
            @PathVariable UUID ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageDto<FileInfoDto> files = fileService.getFilesByOwner(ownerId, page, size);
        return ResponseEntity.ok(ApiResponse.success(files));
    }
    
    /**
     * Get files by parent folder
     *
     * @param folderId the folder ID
     * @param page the page number
     * @param size the page size
     * @return the files
     */
    @GetMapping("/folders/{folderId}/files")
    @Operation(summary = "Get files by parent folder", description = "Get files in a specific folder")
    public ResponseEntity<ApiResponse<PageDto<FileInfoDto>>> getFilesByParentFolder(
            @PathVariable UUID folderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to view folder
        if (!fileService.hasPermission(folderId, currentUserId, PermissionType.READ)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to view this folder"));
        }
        
        PageDto<FileInfoDto> files = fileService.getFilesByParentFolder(folderId, page, size);
        return ResponseEntity.ok(ApiResponse.success(files));
    }
    
    /**
     * Get folder tree
     *
     * @param folderId the folder ID
     * @return the folder tree
     */
    @GetMapping("/folders/{folderId}/tree")
    @Operation(summary = "Get folder tree", description = "Get the folder tree starting from a specific folder")
    public ResponseEntity<ApiResponse<List<FileInfoDto>>> getFolderTree(@PathVariable UUID folderId) {
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to view folder
        if (!fileService.hasPermission(folderId, currentUserId, PermissionType.READ)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to view this folder"));
        }
        
        List<FileInfoDto> folderTree = fileService.getFolderTree(folderId);
        return ResponseEntity.ok(ApiResponse.success(folderTree));
    }
    
    /**
     * Search files
     *
     * @param query the search query
     * @param fileType the file type (optional)
     * @param folderId the folder ID (optional)
     * @param page the page number
     * @param size the page size
     * @return the search results
     */
    @GetMapping("/files/search")
    @Operation(summary = "Search files", description = "Search files by name, type, and folder")
    public ResponseEntity<ApiResponse<PageDto<FileInfoDto>>> searchFiles(
            @RequestParam String query,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) UUID folderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        PageDto<FileInfoDto> searchResults = fileService.searchFiles(query, fileType, folderId, currentUserId, page, size);
        return ResponseEntity.ok(ApiResponse.success(searchResults));
    }
    
    /**
     * Share file with user
     *
     * @param fileId the file ID
     * @param request the share request
     * @return the file permission
     */
    @PostMapping("/files/{fileId}/share")
    @Operation(summary = "Share file with user", description = "Share a file with another user with specific permissions")
    public ResponseEntity<ApiResponse<FilePermissionDto>> shareFile(
            @PathVariable UUID fileId,
            @Valid @RequestBody ShareFileRequest request) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to share
        if (!fileService.hasPermission(fileId, currentUserId, PermissionType.OWNER)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to share this file"));
        }
        
        FilePermission permission = fileService.shareFile(fileId, request.getUserId(), request.getPermissionType());
        return ResponseEntity.ok(ApiResponse.success(filePermissionMapper.toDto(permission)));
    }
    
    /**
     * Get file permissions
     *
     * @param fileId the file ID
     * @return the file permissions
     */
    @GetMapping("/files/{fileId}/permissions")
    @Operation(summary = "Get file permissions", description = "Get all permissions for a file")
    public ResponseEntity<ApiResponse<List<FilePermissionDto>>> getFilePermissions(@PathVariable UUID fileId) {
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to view permissions
        if (!fileService.hasPermission(fileId, currentUserId, PermissionType.OWNER)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to view permissions for this file"));
        }
        
        List<FilePermission> permissions = fileService.getFilePermissions(fileId);
        return ResponseEntity.ok(ApiResponse.success(filePermissionMapper.toDtoList(permissions)));
    }
    
    /**
     * Remove file sharing
     *
     * @param fileId the file ID
     * @param userId the user ID
     * @return the response
     */
    @DeleteMapping("/files/{fileId}/share/{userId}")
    @Operation(summary = "Remove file sharing", description = "Remove file sharing for a specific user")
    public ResponseEntity<ApiResponse<Void>> removeFileSharing(
            @PathVariable UUID fileId,
            @PathVariable UUID userId) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to remove sharing
        if (!fileService.hasPermission(fileId, currentUserId, PermissionType.OWNER)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to remove sharing for this file"));
        }
        
        fileService.removeFileSharing(fileId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    /**
     * Update folder (rename or move)
     *
     * @param folderId the folder ID
     * @param newName the new name (optional)
     * @param newParentId the new parent folder ID (optional)
     * @return the updated folder info
     */
    @PutMapping("/folders/{folderId}")
    @Operation(summary = "Update folder", description = "Rename or move a folder")
    public ResponseEntity<ApiResponse<FileInfoDto>> updateFolder(
            @PathVariable UUID folderId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) UUID newParentId) {
        
        // Get current user ID from security context
        UUID currentUserId = UUID.randomUUID(); // TODO: Get from security context
        
        // Check if user has permission to update folder
        if (!fileService.hasPermission(folderId, currentUserId, PermissionType.WRITE)) {
            return ResponseEntity.status(403).body(ApiResponse.error("You don't have permission to update this folder"));
        }
        
        FileInfoDto updatedFolder = fileService.updateFolder(folderId, newName, newParentId);
        return ResponseEntity.ok(ApiResponse.success(updatedFolder));
    }
}