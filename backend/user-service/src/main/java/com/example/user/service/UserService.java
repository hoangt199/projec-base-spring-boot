package com.example.user.service;

import com.example.common.dto.PageDto;
import com.example.common.exception.ResourceNotFoundException;
import com.example.user.dto.UserDto;
import com.example.user.entity.User;
import com.example.user.mapper.UserMappInf;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMappInf userMapper;

    public PageDto<UserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAll(pageable);
        
        List<UserDto> userDtos = userPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        
        return PageDto.of(userDtos, userPage.getTotalElements(), page, size);
    }

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(UUID id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        
        userMapper.updateUserFromDto(userDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id.toString());
        }
        userRepository.deleteById(id);
    }
}