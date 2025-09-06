package com.example.file.mapper;

import com.example.file.dto.FileInfoDto;
import com.example.file.entity.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link FileInfo} and its DTO {@link FileInfoDto}.
 */
@Mapper(componentModel = "spring")
public interface FileInfoMapper {

    /**
     * Convert entity to DTO
     *
     * @param entity the entity
     * @return the DTO
     */
    FileInfoDto toDto(FileInfo entity);

    /**
     * Convert DTO to entity
     *
     * @param dto the DTO
     * @return the entity
     */
    FileInfo toEntity(FileInfoDto dto);
}