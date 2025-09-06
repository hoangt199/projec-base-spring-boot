package com.example.user.mapper;

import com.example.user.dto.PermissionDto;
import com.example.user.dto.RoleDto;
import com.example.user.dto.UserDto;
import com.example.user.entity.Permission;
import com.example.user.entity.RoleEntity;
import com.example.user.entity.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T23:46:21+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class UserMappInfImpl implements UserMappInf {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );
        userDto.firstName( user.getFirstName() );
        userDto.lastName( user.getLastName() );
        userDto.phoneNumber( user.getPhoneNumber() );
        userDto.provider( user.getProvider() );
        userDto.providerId( user.getProviderId() );
        userDto.avatarUrl( user.getAvatarUrl() );
        userDto.createdAt( user.getCreatedAt() );
        userDto.updatedAt( user.getUpdatedAt() );
        userDto.role( roleEntityToRoleDto( user.getRole() ) );

        return userDto.build();
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder<?, ?> user = User.builder();

        user.id( dto.getId() );
        user.createdAt( dto.getCreatedAt() );
        user.updatedAt( dto.getUpdatedAt() );
        user.username( dto.getUsername() );
        user.email( dto.getEmail() );
        user.firstName( dto.getFirstName() );
        user.lastName( dto.getLastName() );
        user.phoneNumber( dto.getPhoneNumber() );
        user.role( roleDtoToRoleEntity( dto.getRole() ) );
        user.provider( dto.getProvider() );
        user.providerId( dto.getProviderId() );
        user.avatarUrl( dto.getAvatarUrl() );

        return user.build();
    }

    @Override
    public void updateUserFromDto(UserDto userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        user.setId( userDto.getId() );
        user.setCreatedAt( userDto.getCreatedAt() );
        user.setUpdatedAt( userDto.getUpdatedAt() );
        user.setUsername( userDto.getUsername() );
        user.setEmail( userDto.getEmail() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setPhoneNumber( userDto.getPhoneNumber() );
        if ( userDto.getRole() != null ) {
            if ( user.getRole() == null ) {
                user.setRole( RoleEntity.builder().build() );
            }
            roleDtoToRoleEntity1( userDto.getRole(), user.getRole() );
        }
        else {
            user.setRole( null );
        }
        user.setProvider( userDto.getProvider() );
        user.setProviderId( userDto.getProviderId() );
        user.setAvatarUrl( userDto.getAvatarUrl() );
    }

    protected PermissionDto permissionToPermissionDto(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDto.PermissionDtoBuilder permissionDto = PermissionDto.builder();

        permissionDto.id( permission.getId() );
        permissionDto.name( permission.getName() );
        permissionDto.description( permission.getDescription() );
        permissionDto.createdAt( permission.getCreatedAt() );
        permissionDto.updatedAt( permission.getUpdatedAt() );

        return permissionDto.build();
    }

    protected Set<PermissionDto> permissionSetToPermissionDtoSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionDto> set1 = new LinkedHashSet<PermissionDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( permissionToPermissionDto( permission ) );
        }

        return set1;
    }

    protected RoleDto roleEntityToRoleDto(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        RoleDto.RoleDtoBuilder roleDto = RoleDto.builder();

        roleDto.id( roleEntity.getId() );
        roleDto.name( roleEntity.getName() );
        roleDto.description( roleEntity.getDescription() );
        roleDto.permissions( permissionSetToPermissionDtoSet( roleEntity.getPermissions() ) );
        roleDto.createdAt( roleEntity.getCreatedAt() );
        roleDto.updatedAt( roleEntity.getUpdatedAt() );

        return roleDto.build();
    }

    protected Permission permissionDtoToPermission(PermissionDto permissionDto) {
        if ( permissionDto == null ) {
            return null;
        }

        Permission.PermissionBuilder<?, ?> permission = Permission.builder();

        permission.id( permissionDto.getId() );
        permission.createdAt( permissionDto.getCreatedAt() );
        permission.updatedAt( permissionDto.getUpdatedAt() );
        permission.name( permissionDto.getName() );
        permission.description( permissionDto.getDescription() );

        return permission.build();
    }

    protected Set<Permission> permissionDtoSetToPermissionSet(Set<PermissionDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<Permission> set1 = new LinkedHashSet<Permission>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PermissionDto permissionDto : set ) {
            set1.add( permissionDtoToPermission( permissionDto ) );
        }

        return set1;
    }

    protected RoleEntity roleDtoToRoleEntity(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        RoleEntity.RoleEntityBuilder<?, ?> roleEntity = RoleEntity.builder();

        roleEntity.createdAt( roleDto.getCreatedAt() );
        roleEntity.updatedAt( roleDto.getUpdatedAt() );
        roleEntity.id( roleDto.getId() );
        roleEntity.name( roleDto.getName() );
        roleEntity.description( roleDto.getDescription() );
        roleEntity.permissions( permissionDtoSetToPermissionSet( roleDto.getPermissions() ) );

        return roleEntity.build();
    }

    protected void roleDtoToRoleEntity1(RoleDto roleDto, RoleEntity mappingTarget) {
        if ( roleDto == null ) {
            return;
        }

        mappingTarget.setCreatedAt( roleDto.getCreatedAt() );
        mappingTarget.setUpdatedAt( roleDto.getUpdatedAt() );
        mappingTarget.setName( roleDto.getName() );
        mappingTarget.setDescription( roleDto.getDescription() );
        if ( mappingTarget.getPermissions() != null ) {
            Set<Permission> set = permissionDtoSetToPermissionSet( roleDto.getPermissions() );
            if ( set != null ) {
                mappingTarget.getPermissions().clear();
                mappingTarget.getPermissions().addAll( set );
            }
            else {
                mappingTarget.setPermissions( null );
            }
        }
        else {
            Set<Permission> set = permissionDtoSetToPermissionSet( roleDto.getPermissions() );
            if ( set != null ) {
                mappingTarget.setPermissions( set );
            }
        }
        mappingTarget.setId( roleDto.getId() );
    }
}
