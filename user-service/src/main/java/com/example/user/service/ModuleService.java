package com.example.user.service;

import com.example.user.dto.ModuleDto;
import com.example.user.dto.ModuleRequest;
import com.example.user.entity.Module;
import com.example.user.exception.ResourceAlreadyExistsException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.ModuleMapper;
import com.example.user.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for Module management
 */
@Service
@RequiredArgsConstructor
public class ModuleService {
    
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    
    /**
     * Get all modules
     * 
     * @return the list of modules
     */
    @Transactional(readOnly = true)
    public List<ModuleDto> getAllModules() {
        List<Module> modules = moduleRepository.findAll();
        return moduleMapper.toDtoList(modules);
    }
    
    /**
     * Get all active modules
     * 
     * @return the list of active modules
     */
    @Transactional(readOnly = true)
    public List<ModuleDto> getActiveModules() {
        List<Module> modules = moduleRepository.findByIsActiveTrue();
        return moduleMapper.toDtoList(modules);
    }
    
    /**
     * Get module by ID
     * 
     * @param id the module ID
     * @return the module
     * @throws ResourceNotFoundException if the module is not found
     */
    @Transactional(readOnly = true)
    public ModuleDto getModuleById(UUID id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        return moduleMapper.toDto(module);
    }
    
    /**
     * Get module by key
     * 
     * @param moduleKey the module key
     * @return the module
     * @throws ResourceNotFoundException if the module is not found
     */
    @Transactional(readOnly = true)
    public ModuleDto getModuleByKey(String moduleKey) {
        Module module = moduleRepository.findByModuleKey(moduleKey)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with key: " + moduleKey));
        return moduleMapper.toDto(module);
    }
    
    /**
     * Create a new module
     * 
     * @param request the module request
     * @return the created module
     * @throws ResourceAlreadyExistsException if a module with the same key already exists
     */
    @Transactional
    public ModuleDto createModule(ModuleRequest request) {
        if (moduleRepository.existsByModuleKey(request.getModuleKey())) {
            throw new ResourceAlreadyExistsException("Module already exists with key: " + request.getModuleKey());
        }
        
        Module module = moduleMapper.toEntity(request);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }
    
    /**
     * Update a module
     * 
     * @param id the module ID
     * @param request the module request
     * @return the updated module
     * @throws ResourceNotFoundException if the module is not found
     * @throws ResourceAlreadyExistsException if a module with the same key already exists
     */
    @Transactional
    public ModuleDto updateModule(UUID id, ModuleRequest request) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        
        // Check if the module key is changed and if the new key already exists
        if (!module.getModuleKey().equals(request.getModuleKey()) && 
                moduleRepository.existsByModuleKey(request.getModuleKey())) {
            throw new ResourceAlreadyExistsException("Module already exists with key: " + request.getModuleKey());
        }
        
        module = moduleMapper.updateEntity(request, module);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }
    
    /**
     * Delete a module
     * 
     * @param id the module ID
     * @throws ResourceNotFoundException if the module is not found
     */
    @Transactional
    public void deleteModule(UUID id) {
        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Module not found with id: " + id);
        }
        moduleRepository.deleteById(id);
    }
    
    /**
     * Activate or deactivate a module
     * 
     * @param id the module ID
     * @param active the active status
     * @return the updated module
     * @throws ResourceNotFoundException if the module is not found
     */
    @Transactional
    public ModuleDto setModuleActive(UUID id, boolean active) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        
        module.setActive(active);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }
    
    /**
     * Get modules by user ID
     * 
     * @param userId the user ID
     * @return the list of modules
     */
    @Transactional(readOnly = true)
    public List<ModuleDto> getModulesByUserId(UUID userId) {
        List<Module> modules = moduleRepository.findByUserId(userId);
        return moduleMapper.toDtoList(modules);
    }
}