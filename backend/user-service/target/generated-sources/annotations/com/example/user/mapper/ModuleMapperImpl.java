package com.example.user.mapper;

import com.example.user.dto.ModuleDto;
import com.example.user.dto.ModuleRequest;
import com.example.user.entity.Module;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T23:46:21+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class ModuleMapperImpl implements ModuleMapper {

    @Override
    public ModuleDto toDto(Module module) {
        if ( module == null ) {
            return null;
        }

        ModuleDto.ModuleDtoBuilder moduleDto = ModuleDto.builder();

        moduleDto.id( module.getId() );
        moduleDto.name( module.getName() );
        moduleDto.moduleKey( module.getModuleKey() );
        moduleDto.description( module.getDescription() );
        moduleDto.createdAt( module.getCreatedAt() );
        moduleDto.updatedAt( module.getUpdatedAt() );

        return moduleDto.build();
    }

    @Override
    public List<ModuleDto> toDtoList(List<Module> modules) {
        if ( modules == null ) {
            return null;
        }

        List<ModuleDto> list = new ArrayList<ModuleDto>( modules.size() );
        for ( Module module : modules ) {
            list.add( toDto( module ) );
        }

        return list;
    }

    @Override
    public Module toEntity(ModuleRequest request) {
        if ( request == null ) {
            return null;
        }

        Module.ModuleBuilder<?, ?> module = Module.builder();

        module.name( request.getName() );
        module.moduleKey( request.getModuleKey() );
        module.description( request.getDescription() );

        return module.build();
    }

    @Override
    public Module updateEntity(ModuleRequest request, Module module) {
        if ( request == null ) {
            return module;
        }

        module.setName( request.getName() );
        module.setModuleKey( request.getModuleKey() );
        module.setDescription( request.getDescription() );
        module.setActive( request.getActive() );

        return module;
    }
}
