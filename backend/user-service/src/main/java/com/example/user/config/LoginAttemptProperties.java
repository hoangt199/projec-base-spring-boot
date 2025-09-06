package com.example.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for login attempt management
 */
@Configuration
@ConfigurationProperties(prefix = "app.security.login-attempt")
@Getter
@Setter
public class LoginAttemptProperties {

    /**
     * Maximum number of failed login attempts before account is locked
     */
    private int maxAttempts = 5;

    /**
     * Time window in minutes to count failed attempts
     */
    private int attemptTimeWindowMinutes = 10;

    /**
     * Duration in minutes for which account remains locked after exceeding max attempts
     */
    private int lockDurationMinutes = 1;
}