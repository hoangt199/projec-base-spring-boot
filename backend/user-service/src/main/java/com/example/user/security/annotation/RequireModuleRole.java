package com.example.user.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check if the current user has the required role in the specified module
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireModuleRole {
    
    /**
     * The module key
     * 
     * @return the module key
     */
    String moduleKey();
    
    /**
     * The role name
     * 
     * @return the role name
     */
    String role();
}