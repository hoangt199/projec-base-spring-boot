package com.example.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for file information
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {

    private UUID id;
    private String originalName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private String mediaType;
    private String extension;
    private boolean isFolder;
    private UUID parentFolderId;
    private UUID ownerId;
    private UUID groupId;
    private boolean isPublic;
    private String accessPermissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}