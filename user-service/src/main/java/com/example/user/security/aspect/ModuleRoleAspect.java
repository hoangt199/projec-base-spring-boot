package com.example.user.security.aspect;

import com.example.user.entity.User;
import com.example.user.exception.AccessDeniedException;
import com.example.user.security.annotation.RequireModuleRole;
import com.example.user.service.UserModuleRoleService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Aspect to check module roles
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ModuleRoleAspect {
    
    private final UserModuleRoleService userModuleRoleService;
    
    /**
     * Check if the current user has the required role in the specified module
     * 
     * @param joinPoint the join point
     * @param requireModuleRole the annotation
     */
    @Before("@annotation(requireModuleRole)")
    public void checkModuleRole(JoinPoint joinPoint, RequireModuleRole requireModuleRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new AccessDeniedException("Invalid authentication principal");
        }
        
        User user = (User) principal;
        String moduleKey = requireModuleRole.moduleKey();
        String role = requireModuleRole.role();
        
        // Check if the user has the required role in the module
        if (!userModuleRoleService.hasModuleRole(user.getId(), moduleKey, role)) {
            throw new AccessDeniedException("User does not have the required role in the module");
        }
    }
}