package com.example.user.mapper;

import com.example.user.dto.MenuDto;
import com.example.user.dto.request.MenuRequest;
import com.example.user.entity.Menu;
import com.example.user.entity.Module;
import com.example.user.entity.Router;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for Menu entity and DTOs
 */
@Component
public class MenuMapper {

    /**
     * Convert Menu entity to MenuDto
     * @param menu Menu entity
     * @return MenuDto
     */
    public MenuDto toDto(Menu menu) {
        if (menu == null) {
            return null;
        }
        
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .icon(menu.getIcon())
                .routerId(menu.getRouter() != null ? menu.getRouter().getId() : null)
                .routerPath(menu.getRouter() != null ? menu.getRouter().getPath() : null)
                .routerName(menu.getRouter() != null ? menu.getRouter().getName() : null)
                .moduleId(menu.getModule() != null ? menu.getModule().getId() : null)
                .moduleName(menu.getModule() != null ? menu.getModule().getName() : null)
                .moduleKey(menu.getModule() != null ? menu.getModule().getModuleKey() : null)
                .parentId(menu.getParentId())
                .displayOrder(menu.getDisplayOrder())
                .active(menu.isActive())
                .visible(menu.isVisible())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }

    /**
     * Convert list of Menu entities to list of MenuDtos
     * @param menus List of Menu entities
     * @return List of MenuDtos
     */
    public List<MenuDto> toDtoList(List<Menu> menus) {
        if (menus == null) {
            return List.of();
        }
        
        return menus.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert MenuRequest to Menu entity
     * @param request MenuRequest
     * @param module Module entity
     * @param router Router entity
     * @return Menu entity
     */
    public Menu toEntity(MenuRequest request, Module module, Router router) {
        if (request == null) {
            return null;
        }
        
        return Menu.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .router(router)
                .module(module)
                .parentId(request.getParentId())
                .displayOrder(request.getDisplayOrder())
                .active(request.getActive())
                .visible(request.getVisible())
                .build();
    }

    /**
     * Update Menu entity from MenuRequest
     * @param menu Menu entity to update
     * @param request MenuRequest with new values
     * @param module Module entity
     * @param router Router entity
     * @return Updated Menu entity
     */
    public Menu updateEntity(Menu menu, MenuRequest request, Module module, Router router) {
        if (menu == null || request == null) {
            return menu;
        }
        
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setIcon(request.getIcon());
        menu.setRouter(router);
        menu.setModule(module);
        menu.setParentId(request.getParentId());
        menu.setDisplayOrder(request.getDisplayOrder());
        menu.setActive(request.getActive());
        menu.setVisible(request.getVisible());
        
        return menu;
    }
}