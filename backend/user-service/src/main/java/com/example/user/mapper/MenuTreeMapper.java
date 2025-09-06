package com.example.user.mapper;

import com.example.user.dto.MenuDto;
import com.example.user.dto.MenuTreeDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Mapper for converting MenuDto to MenuTreeDto with tree structure
 */
@Component
public class MenuTreeMapper {

    /**
     * Convert a list of MenuDto to a tree structure of MenuTreeDto
     * @param menuDtos list of MenuDto
     * @return list of root MenuTreeDto with children
     */
    public List<MenuTreeDto> toMenuTree(List<MenuDto> menuDtos) {
        // Create a map of all menus by ID for quick lookup
        Map<UUID, MenuTreeDto> menuMap = new HashMap<>();
        
        // First pass: convert all MenuDto to MenuTreeDto and store in map
        for (MenuDto menuDto : menuDtos) {
            MenuTreeDto menuTreeDto = MenuTreeDto.builder()
                    .id(menuDto.getId())
                    .name(menuDto.getName())
                    .description(menuDto.getDescription())
                    .icon(menuDto.getIcon())
                    .router(menuDto.getRouter())
                    .module(menuDto.getModule())
                    .parentId(menuDto.getParentId())
                    .displayOrder(menuDto.getDisplayOrder())
                    .active(menuDto.isActive())
                    .visible(menuDto.isVisible())
                    .children(new ArrayList<>())
                    .build();
            
            menuMap.put(menuDto.getId(), menuTreeDto);
        }
        
        // Second pass: build the tree structure
        List<MenuTreeDto> rootMenus = new ArrayList<>();
        
        for (MenuDto menuDto : menuDtos) {
            MenuTreeDto menuTreeDto = menuMap.get(menuDto.getId());
            
            if (menuDto.getParentId() == null) {
                // This is a root menu
                rootMenus.add(menuTreeDto);
            } else {
                // This is a child menu, add it to its parent
                MenuTreeDto parentMenu = menuMap.get(menuDto.getParentId());
                if (parentMenu != null) {
                    parentMenu.getChildren().add(menuTreeDto);
                } else {
                    // Parent not found, treat as root
                    rootMenus.add(menuTreeDto);
                }
            }
        }
        
        // Sort root menus by display order
        rootMenus.sort((m1, m2) -> {
            if (m1.getDisplayOrder() == null && m2.getDisplayOrder() == null) {
                return 0;
            } else if (m1.getDisplayOrder() == null) {
                return 1;
            } else if (m2.getDisplayOrder() == null) {
                return -1;
            }
            return m1.getDisplayOrder().compareTo(m2.getDisplayOrder());
        });
        
        // Sort children by display order
        sortChildrenByDisplayOrder(rootMenus);
        
        return rootMenus;
    }
    
    /**
     * Recursively sort children by display order
     * @param menus list of menus to sort children
     */
    private void sortChildrenByDisplayOrder(List<MenuTreeDto> menus) {
        for (MenuTreeDto menu : menus) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                menu.getChildren().sort((m1, m2) -> {
                    if (m1.getDisplayOrder() == null && m2.getDisplayOrder() == null) {
                        return 0;
                    } else if (m1.getDisplayOrder() == null) {
                        return 1;
                    } else if (m2.getDisplayOrder() == null) {
                        return -1;
                    }
                    return m1.getDisplayOrder().compareTo(m2.getDisplayOrder());
                });
                
                // Recursively sort children
                sortChildrenByDisplayOrder(menu.getChildren());
            }
        }
    }
}