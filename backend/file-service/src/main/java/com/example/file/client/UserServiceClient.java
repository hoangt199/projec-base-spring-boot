package com.example.file.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

/**
 * Feign client for User Service
 */
@FeignClient(name = "user-service")
public interface UserServiceClient {

    /**
     * Get user by ID
     *
     * @param userId the user ID
     * @return the user data
     */
    @GetMapping("/api/users/{userId}")
    Map<String, Object> getUserById(@PathVariable("userId") UUID userId);
    
    /**
     * Check if user exists
     *
     * @param userId the user ID
     * @return true if user exists
     */
    @GetMapping("/api/users/{userId}/exists")
    boolean userExists(@PathVariable("userId") UUID userId);
}