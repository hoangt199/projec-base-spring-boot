package com.example.user.mapper;

import com.example.user.dto.UserDto;
import com.example.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMappInf {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);

}
