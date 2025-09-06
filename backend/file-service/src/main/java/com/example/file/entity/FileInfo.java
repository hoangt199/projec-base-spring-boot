package com.example.file.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entity for storing file information
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_info")
@NamedQueries({
    @NamedQuery(name = "FileInfo.findByExtension", 
               query = "SELECT f FROM FileInfo f WHERE f.extension = :extension AND f.isDeleted = false"),
    @NamedQuery(name = "FileInfo.findByMediaType", 
               query = "SELECT f FROM FileInfo f WHERE f.mediaType = :mediaType AND f.isDeleted = false")
})
public class FileInfo extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "original_name", nullable = false)
    private String originalName;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(name = "content_type")
    private String contentType;
    
    @Column(name = "media_type")
    private String mediaType;
    
    @Column(name = "extension")
    private String extension;
    
    @Column(name = "is_folder")
    private boolean isFolder;
    
    @Column(name = "parent_folder_id")
    private UUID parentFolderId;
    
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
    
    @Column(name = "group_id")
    private UUID groupId;
    
    @Column(name = "is_public")
    private boolean isPublic;
    
    @Column(name = "access_permissions")
    private String accessPermissions;
    
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FilePermission> permissions = new HashSet<>();
    
    /**
     * Add permission to this file
     * 
     * @param permission the permission to add
     * @return the file info
     */
    public FileInfo addPermission(FilePermission permission) {
        permissions.add(permission);
        permission.setFile(this);
        return this;
    }
    
    /**
     * Remove permission from this file
     * 
     * @param permission the permission to remove
     * @return the file info
     */
    public FileInfo removePermission(FilePermission permission) {
        permissions.remove(permission);
        permission.setFile(null);
        return this;
    }
}