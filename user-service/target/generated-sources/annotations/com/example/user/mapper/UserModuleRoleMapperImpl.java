package com.example.user.mapper;

import com.example.user.dto.UserModuleRoleDto;
import com.example.user.entity.Module;
import com.example.user.entity.RoleEntity;
import com.example.user.entity.User;
import com.example.user.entity.UserModuleRole;
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
public class UserModuleRoleMapperImpl implements UserModuleRoleMapper {

    @Override
    public UserModuleRoleDto toDto(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }

        UserModuleRoleDto.UserModuleRoleDtoBuilder userModuleRoleDto = UserModuleRoleDto.builder();

        userModuleRoleDto.userId( userModuleRoleUserId( userModuleRole ) );
        userModuleRoleDto.username( userModuleRoleUserUsername( userModuleRole ) );
        userModuleRoleDto.moduleId( userModuleRoleModuleId( userModuleRole ) );
        userModuleRoleDto.moduleName( userModuleRoleModuleName( userModuleRole ) );
        userModuleRoleDto.moduleKey( userModuleRoleModuleModuleKey( userModuleRole ) );
        userModuleRoleDto.roleId( userModuleRoleRoleId( userModuleRole ) );
        userModuleRoleDto.roleName( userModuleRoleRoleName( userModuleRole ) );
        userModuleRoleDto.id( userModuleRole.getId() );
        userModuleRoleDto.createdAt( userModuleRole.getCreatedAt() );
        userModuleRoleDto.updatedAt( userModuleRole.getUpdatedAt() );

        return userModuleRoleDto.build();
    }

    @Override
    public List<UserModuleRoleDto> toDtoList(List<UserModuleRole> userModuleRoles) {
        if ( userModuleRoles == null ) {
            return null;
        }

        List<UserModuleRoleDto> list = new ArrayList<UserModuleRoleDto>( userModuleRoles.size() );
        for ( UserModuleRole userModuleRole : userModuleRoles ) {
            list.add( toDto( userModuleRole ) );
        }

        return list;
    }

    private Long userModuleRoleUserId(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        User user = userModuleRole.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String userModuleRoleUserUsername(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        User user = userModuleRole.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private Long userModuleRoleModuleId(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        Module module = userModuleRole.getModule();
        if ( module == null ) {
            return null;
        }
        Long id = module.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String userModuleRoleModuleName(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        Module module = userModuleRole.getModule();
        if ( module == null ) {
            return null;
        }
        String name = module.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String userModuleRoleModuleModuleKey(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        Module module = userModuleRole.getModule();
        if ( module == null ) {
            return null;
        }
        String moduleKey = module.getModuleKey();
        if ( moduleKey == null ) {
            return null;
        }
        return moduleKey;
    }

    private Long userModuleRoleRoleId(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        RoleEntity role = userModuleRole.getRole();
        if ( role == null ) {
            return null;
        }
        Long id = role.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String userModuleRoleRoleName(UserModuleRole userModuleRole) {
        if ( userModuleRole == null ) {
            return null;
        }
        RoleEntity role = userModuleRole.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
