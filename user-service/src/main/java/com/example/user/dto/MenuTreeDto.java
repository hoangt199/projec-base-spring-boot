package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO for menu tree structure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeDto {
    
    /**
     * Menu ID
     */
    private UUID id;
    
    /**
     * Menu name
     */
    private String name;
    
    /**
     * Menu description
     */
    private String description;
    
    /**
     * Menu icon
     */
    private String icon;
    
    /**
     * Router information
     */
    private RouterDto router;
    
    /**
     * Module information
     */
    private ModuleDto module;
    
    /**
     * Parent menu ID
     */
    private UUID parentId;
    
    /**
     * Display order
     */
    private Integer displayOrder;
    
    /**
     * Is menu active
     */
    private boolean active;
    
    /**
     * Is menu visible
     */
    private boolean visible;
    
    /**
     * Child menus
     */
    private List<MenuTreeDto> children;
}