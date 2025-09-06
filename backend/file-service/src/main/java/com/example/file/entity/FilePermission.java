package com.example.file.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Entity for storing file permissions
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_permission", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"file_id", "user_id"}))
public class FilePermission extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileInfo file;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    /**
     * Permission type: READ, WRITE, OWNER
     */
    @Column(name = "permission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    
    /**
     * Permission types
     */
    public enum PermissionType {
        READ,
        WRITE,
        OWNER
    }
}