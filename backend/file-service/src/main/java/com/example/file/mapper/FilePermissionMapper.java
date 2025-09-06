package com.example.file.mapper;

import com.example.file.dto.FilePermissionDto;
import com.example.file.entity.FilePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for FilePermission entity and DTO
 */
@Mapper(componentModel = "spring")
public interface FilePermissionMapper {

    FilePermissionMapper INSTANCE = Mappers.getMapper(FilePermissionMapper.class);

    /**
     * Convert entity to DTO
     *
     * @param permission the entity
     * @return the DTO
     */
    @Mapping(source = "file.id", target = "fileId")
    FilePermissionDto toDto(FilePermission permission);

    /**
     * Convert DTO to entity
     *
     * @param dto the DTO
     * @return the entity
     */
    @Mapping(target = "file.id", source = "fileId")
    @Mapping(target = "isDeleted", ignore = true)
    FilePermission toEntity(FilePermissionDto dto);

    /**
     * Convert entity list to DTO list
     *
     * @param permissions the entity list
     * @return the DTO list
     */
    List<FilePermissionDto> toDtoList(List<FilePermission> permissions);
}