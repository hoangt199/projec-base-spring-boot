package com.example.user.mapper;

import com.example.user.dto.RouterDto;
import com.example.user.dto.request.RouterRequest;
import com.example.user.entity.Module;
import com.example.user.entity.Router;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for Router entity and DTOs
 */
@Component
public class RouterMapper {

    /**
     * Convert Router entity to RouterDto
     * @param router Router entity
     * @return RouterDto
     */
    public RouterDto toDto(Router router) {
        if (router == null) {
            return null;
        }
        
        return RouterDto.builder()
                .id(router.getId())
                .path(router.getPath())
                .name(router.getName())
                .component(router.getComponent())
                .redirect(router.getRedirect())
                .icon(router.getIcon())
                .hidden(router.isHidden())
                .moduleId(router.getModule() != null ? router.getModule().getId() : null)
                .moduleName(router.getModule() != null ? router.getModule().getName() : null)
                .moduleKey(router.getModule() != null ? router.getModule().getModuleKey() : null)
                .parentId(router.getParentId())
                .displayOrder(router.getDisplayOrder())
                .active(router.isActive())
                .createdAt(router.getCreatedAt())
                .updatedAt(router.getUpdatedAt())
                .build();
    }

    /**
     * Convert list of Router entities to list of RouterDtos
     * @param routers List of Router entities
     * @return List of RouterDtos
     */
    public List<RouterDto> toDtoList(List<Router> routers) {
        if (routers == null) {
            return List.of();
        }
        
        return routers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert RouterRequest to Router entity
     * @param request RouterRequest
     * @param module Module entity
     * @return Router entity
     */
    public Router toEntity(RouterRequest request, Module module) {
        if (request == null) {
            return null;
        }
        
        return Router.builder()
                .path(request.getPath())
                .name(request.getName())
                .component(request.getComponent())
                .redirect(request.getRedirect())
                .icon(request.getIcon())
                .hidden(request.isHidden())
                .module(module)
                .parentId(request.getParentId())
                .displayOrder(request.getDisplayOrder())
                .active(request.isActive())
                .build();
    }

    /**
     * Update Router entity from RouterRequest
     * @param router Router entity to update
     * @param request RouterRequest with new values
     * @param module Module entity
     * @return Updated Router entity
     */
    public Router updateEntity(Router router, RouterRequest request, Module module) {
        if (router == null || request == null) {
            return router;
        }
        
        router.setPath(request.getPath());
        router.setName(request.getName());
        router.setComponent(request.getComponent());
        router.setRedirect(request.getRedirect());
        router.setIcon(request.getIcon());
        router.setHidden(request.isHidden());
        router.setModule(module);
        router.setParentId(request.getParentId());
        router.setDisplayOrder(request.getDisplayOrder());
        router.setActive(request.isActive());
        
        return router;
    }
}