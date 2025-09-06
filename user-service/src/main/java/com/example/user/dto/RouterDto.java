package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Router entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterDto {
    private UUID id;
    private String path;
    private String name;
    private String component;
    private String redirect;
    private String icon;
    private boolean hidden;
    private UUID moduleId;
    private String moduleName;
    private String moduleKey;
    private UUID parentId;
    private Integer displayOrder;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}