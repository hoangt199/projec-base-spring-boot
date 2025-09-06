package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.entity.UserModuleRole;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for managing user module permissions and roles
 * Contains business logic extracted from User entity to follow SOLID principles
 */
@Service
@RequiredArgsConstructor
public class UserModulePermissionService {

    private final UserRepository userRepository;
}