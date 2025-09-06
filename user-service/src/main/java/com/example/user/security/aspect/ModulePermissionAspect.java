package com.example.user.security.aspect;

import com.example.user.entity.User;
import com.example.user.exception.AccessDeniedException;
import com.example.user.security.annotation.RequireModulePermission;
import com.example.user.service.UserModuleRoleService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Aspect to check module permissions
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ModulePermissionAspect {
    
    private final UserModuleRoleService userModuleRoleService;
    
    /**
     * Check if the current user has the required permission in the specified module
     * 
     * @param joinPoint the join point
     * @param requireModulePermission the annotation
     */
    @Before("@annotation(requireModulePermission)")
    public void checkModulePermission(JoinPoint joinPoint, RequireModulePermission requireModulePermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new AccessDeniedException("Invalid authentication principal");
        }
        
        User user = (User) principal;
        String moduleKey = requireModulePermission.moduleKey();
        String permission = requireModulePermission.permission();
        
        // Check if the user has the required permission in the module
        if (!userModuleRoleService.hasModulePermission(user.getId(), moduleKey, permission)) {
            throw new AccessDeniedException("User does not have the required permission in the module");
        }
    }
}