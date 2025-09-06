package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for module menu structure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleMenuDto {
    
    /**
     * Module information
     */
    private ModuleDto module;
    
    /**
     * List of menus for this module
     */
    private List<MenuDto> menus;
    
    /**
     * List of routers for this module
     */
    private List<RouterDto> routers;
    
    /**
     * Menu tree structure for this module
     */
    private List<MenuTreeDto> menuTree;
}