package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Menu entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private UUID id;
    private String name;
    private String description;
    private String icon;
    private UUID routerId;
    private String routerPath;
    private String routerName;
    private UUID moduleId;
    private String moduleName;
    private String moduleKey;
    private UUID parentId;
    private Integer displayOrder;
    private boolean active;
    private boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}