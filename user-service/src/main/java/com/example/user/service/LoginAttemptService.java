package com.example.user.service;

import com.example.user.entity.LoginFailureAttempt;
import com.example.user.repository.LoginFailureAttemptRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service to manage login attempts and temporary account locking
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptService {

    private final LoginFailureAttemptRepository loginFailureAttemptRepository;
    
    @Value("${security.login.max-attempts:5}")
    private int maxAttempts;
    
    @Value("${security.login.attempt-time-window-minutes:10}")
    private int attemptTimeWindowMinutes;
    
    @Value("${security.login.lock-duration-minutes:1}")
    private int lockDurationMinutes;
    
    // Cache to store locked usernames and their unlock time
    private final Map<String, LocalDateTime> lockedUsers = new HashMap<>();
    
    /**
     * Record a failed login attempt
     *
     * @param username the username
     * @param request the HTTP request
     */
    public void recordFailedAttempt(String username, HttpServletRequest request) {
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String deviceId = request.getHeader("X-Device-ID");
        
        LoginFailureAttempt attempt = new LoginFailureAttempt();
        attempt.setUsername(username);
        attempt.setIpAddress(ipAddress);
        attempt.setUserAgent(userAgent);
        attempt.setDeviceId(deviceId);
        
        loginFailureAttemptRepository.save(attempt);
        
        checkAndLockAccount(username);
    }
    
    /**
     * Check if a user account is locked
     *
     * @param username the username
     * @return true if the account is locked, false otherwise
     */
    public boolean isAccountLocked(String username) {
        if (lockedUsers.containsKey(username)) {
            LocalDateTime unlockTime = lockedUsers.get(username);
            if (LocalDateTime.now().isAfter(unlockTime)) {
                // Unlock time has passed
                lockedUsers.remove(username);
                return false;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Get the remaining lock time in seconds
     *
     * @param username the username
     * @return remaining lock time in seconds, or 0 if not locked
     */
    public long getRemainingLockTime(String username) {
        if (!lockedUsers.containsKey(username)) {
            return 0;
        }
        
        LocalDateTime unlockTime = lockedUsers.get(username);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(unlockTime)) {
            lockedUsers.remove(username);
            return 0;
        }
        
        return java.time.Duration.between(now, unlockTime).getSeconds();
    }
    
    /**
     * Reset failed attempts for a user after successful login
     *
     * @param username the username
     */
    public void resetFailedAttempts(String username) {
        // We don't actually delete the records, just ignore them for future checks
        // This preserves the audit trail
    }
    
    /**
     * Check if the account should be locked based on recent failed attempts
     *
     * @param username the username
     */
    private void checkAndLockAccount(String username) {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(attemptTimeWindowMinutes);
        int attempts = loginFailureAttemptRepository.countRecentAttemptsByUsername(username, cutoff);
        
        if (attempts >= maxAttempts) {
            // Sử dụng lombok @Slf4j annotation để tạo biến log
            // log.warn("Account locked for user: {} due to {} failed attempts", username, attempts);
            lockedUsers.put(username, LocalDateTime.now().plusMinutes(lockDurationMinutes));
        }
    }
    
    /**
     * Extract client IP address from request
     *
     * @param request the HTTP request
     * @return the client IP address
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}