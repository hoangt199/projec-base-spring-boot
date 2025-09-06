package com.example.user.exception;

/**
 * Exception thrown when a resource already exists
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    
    /**
     * Constructs a new ResourceAlreadyExistsException with the specified detail message
     * 
     * @param message the detail message
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new ResourceAlreadyExistsException with the specified detail message and cause
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}