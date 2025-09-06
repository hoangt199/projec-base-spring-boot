package com.example.user.service;

import com.example.user.dto.RouterDto;
import com.example.user.dto.request.RouterRequest;
import com.example.user.entity.Module;
import com.example.user.entity.Router;
import com.example.user.exception.ResourceAlreadyExistsException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.RouterMapper;
import com.example.user.repository.ModuleRepository;
import com.example.user.repository.RouterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for Router management
 */
@Service
@RequiredArgsConstructor
public class RouterService {

    private final RouterRepository routerRepository;
    private final ModuleRepository moduleRepository;
    private final RouterMapper routerMapper;

    /**
     * Get all routers
     * @return List of RouterDto
     */
    public List<RouterDto> getAllRouters() {
        List<Router> routers = routerRepository.findAll();
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all active routers
     * @return List of RouterDto
     */
    public List<RouterDto> getAllActiveRouters() {
        List<Router> routers = routerRepository.findAll().stream()
                .filter(Router::isActive)
                .toList();
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get router by id
     * @param id router id
     * @return RouterDto
     * @throws ResourceNotFoundException if router not found
     */
    public RouterDto getRouterById(UUID id) {
        Router router = routerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Router not found with id: " + id));
        return routerMapper.toDto(router);
    }

    /**
     * Get router by path
     * @param path router path
     * @return RouterDto
     * @throws ResourceNotFoundException if router not found
     */
    public RouterDto getRouterByPath(String path) {
        Router router = routerRepository.findByPath(path)
                .orElseThrow(() -> new ResourceNotFoundException("Router not found with path: " + path));
        return routerMapper.toDto(router);
    }

    /**
     * Get router by name
     * @param name router name
     * @return RouterDto
     * @throws ResourceNotFoundException if router not found
     */
    public RouterDto getRouterByName(String name) {
        Router router = routerRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Router not found with name: " + name));
        return routerMapper.toDto(router);
    }

    /**
     * Get all routers by module id
     * @param moduleId module id
     * @return List of RouterDto
     */
    public List<RouterDto> getRoutersByModuleId(UUID moduleId) {
        List<Router> routers = routerRepository.findByModuleId(moduleId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all active routers by module id
     * @param moduleId module id
     * @return List of RouterDto
     */
    public List<RouterDto> getActiveRoutersByModuleId(UUID moduleId) {
        List<Router> routers = routerRepository.findByModuleIdAndActiveTrue(moduleId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all routers by parent id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    public List<RouterDto> getRoutersByParentId(UUID parentId) {
        List<Router> routers = routerRepository.findByParentId(parentId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all active routers by parent id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    public List<RouterDto> getActiveRoutersByParentId(UUID parentId) {
        List<Router> routers = routerRepository.findByParentIdAndActiveTrue(parentId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all root routers (parentId is null)
     * @return List of RouterDto
     */
    public List<RouterDto> getRootRouters() {
        List<Router> routers = routerRepository.findByParentIdIsNull();
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all active root routers (parentId is null)
     * @return List of RouterDto
     */
    public List<RouterDto> getActiveRootRouters() {
        List<Router> routers = routerRepository.findByParentIdIsNullAndActiveTrue();
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all routers by module id and parent id
     * @param moduleId module id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    public List<RouterDto> getRoutersByModuleIdAndParentId(UUID moduleId, UUID parentId) {
        List<Router> routers = routerRepository.findByModuleIdAndParentId(moduleId, parentId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all active routers by module id and parent id
     * @param moduleId module id
     * @param parentId parent router id
     * @return List of RouterDto
     */
    public List<RouterDto> getActiveRoutersByModuleIdAndParentId(UUID moduleId, UUID parentId) {
        List<Router> routers = routerRepository.findByModuleIdAndParentIdAndActiveTrue(moduleId, parentId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all root routers by module id (parentId is null)
     * @param moduleId module id
     * @return List of RouterDto
     */
    public List<RouterDto> getRootRoutersByModuleId(UUID moduleId) {
        List<Router> routers = routerRepository.findByModuleIdAndParentIdIsNull(moduleId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all active root routers by module id (parentId is null)
     * @param moduleId module id
     * @return List of RouterDto
     */
    public List<RouterDto> getActiveRootRoutersByModuleId(UUID moduleId) {
        List<Router> routers = routerRepository.findByModuleIdAndParentIdIsNullAndActiveTrue(moduleId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Get all routers by user id
     * @param userId user id
     * @return List of RouterDto
     */
    public List<RouterDto> getRoutersByUserId(UUID userId) {
        List<Router> routers = routerRepository.findAllRoutersByUserId(userId);
        return routerMapper.toDtoList(routers);
    }

    /**
     * Create a new router
     * @param request RouterRequest
     * @return RouterDto
     * @throws ResourceAlreadyExistsException if router with same path or name already exists
     * @throws ResourceNotFoundException if module not found
     */
    @Transactional
    public RouterDto createRouter(RouterRequest request) {
        // Check if router with same path already exists
        if (routerRepository.existsByPath(request.getPath())) {
            throw new ResourceAlreadyExistsException("Router already exists with path: " + request.getPath());
        }
        
        // Check if router with same name already exists
        if (routerRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Router already exists with name: " + request.getName());
        }
        
        // Get module
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));
        
        // Create router
        Router router = routerMapper.toEntity(request, module);
        router = routerRepository.save(router);
        
        return routerMapper.toDto(router);
    }

    /**
     * Update router
     * @param id router id
     * @param request RouterRequest
     * @return RouterDto
     * @throws ResourceNotFoundException if router or module not found
     * @throws ResourceAlreadyExistsException if router with same path or name already exists
     */
    @Transactional
    public RouterDto updateRouter(UUID id, RouterRequest request) {
        // Get router
        Router router = routerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Router not found with id: " + id));
        
        // Check if router with same path already exists (excluding current router)
        if (!router.getPath().equals(request.getPath()) && routerRepository.existsByPath(request.getPath())) {
            throw new ResourceAlreadyExistsException("Router already exists with path: " + request.getPath());
        }
        
        // Check if router with same name already exists (excluding current router)
        if (!router.getName().equals(request.getName()) && routerRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Router already exists with name: " + request.getName());
        }
        
        // Get module
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));
        
        // Update router
        router = routerMapper.updateEntity(router, request, module);
        router = routerRepository.save(router);
        
        return routerMapper.toDto(router);
    }

    /**
     * Delete router
     * @param id router id
     * @throws ResourceNotFoundException if router not found
     */
    @Transactional
    public void deleteRouter(UUID id) {
        // Check if router exists
        if (!routerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Router not found with id: " + id);
        }
        
        // Delete router
        routerRepository.deleteById(id);
    }

    /**
     * Set router active status
     * @param id router id
     * @param active active status
     * @return RouterDto
     * @throws ResourceNotFoundException if router not found
     */
    @Transactional
    public RouterDto setRouterActive(UUID id, boolean active) {
        // Get router
        Router router = routerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Router not found with id: " + id));
        
        // Set active status
        router.setActive(active);
        router = routerRepository.save(router);
        
        return routerMapper.toDto(router);
    }
}