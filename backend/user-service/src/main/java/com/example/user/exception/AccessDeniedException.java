package com.example.user.exception;

/**
 * Exception thrown when a user does not have the required permission to access a resource
 */
public class AccessDeniedException extends RuntimeException {
    
    /**
     * Constructs a new AccessDeniedException with the specified detail message
     * 
     * @param message the detail message
     */
    public AccessDeniedException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new AccessDeniedException with the specified detail message and cause
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}