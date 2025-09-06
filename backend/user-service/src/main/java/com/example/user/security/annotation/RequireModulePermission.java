package com.example.user.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check if the current user has the required permission in the specified module
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireModulePermission {
    
    /**
     * The module key
     * 
     * @return the module key
     */
    String moduleKey();
    
    /**
     * The permission name
     * 
     * @return the permission name
     */
    String permission();
}