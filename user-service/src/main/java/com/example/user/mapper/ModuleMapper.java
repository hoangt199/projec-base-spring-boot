package com.example.user.mapper;

import com.example.user.dto.ModuleDto;
import com.example.user.dto.ModuleRequest;
import com.example.user.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper for Module entity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper {
    
    /**
     * Convert Module entity to ModuleDto
     * 
     * @param module the Module entity
     * @return the ModuleDto
     */
    ModuleDto toDto(Module module);
    
    /**
     * Convert list of Module entities to list of ModuleDtos
     * 
     * @param modules the list of Module entities
     * @return the list of ModuleDtos
     */
    List<ModuleDto> toDtoList(List<Module> modules);
    
    /**
     * Convert ModuleRequest to Module entity
     * 
     * @param request the ModuleRequest
     * @return the Module entity
     */
    Module toEntity(ModuleRequest request);
    
    /**
     * Update Module entity from ModuleRequest
     * 
     * @param request the ModuleRequest
     * @param module the Module entity to update
     * @return the updated Module entity
     */
    Module updateEntity(ModuleRequest request, @MappingTarget Module module);
}