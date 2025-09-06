package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.entity.UserSession;
import com.example.user.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service to manage user sessions for multi-device login support
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;
    
    @Value("${security.jwt.refresh-token.expiration-days:7}")
    private int refreshTokenExpirationDays;
    
    /**
     * Create a new user session
     *
     * @param user the user
     * @param refreshToken the refresh token
     * @param deviceId the device ID
     * @param ipAddress the IP address
     * @param userAgent the user agent
     * @param expiresAt the expiration date
     * @return the created user session
     */
    @Transactional
    public UserSession createSession(User user, String refreshToken, String deviceId, String ipAddress, 
                                     String userAgent, LocalDateTime expiresAt) {
        // Create a new session
        UserSession session = new UserSession();
        session.setUser(user);
        session.setRefreshToken(refreshToken);
        session.setDeviceId(deviceId);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setExpiresAt(expiresAt);
        
        return userSessionRepository.save(session);
    }
    
    /**
     * Create a new user session from HTTP request
     *
     * @param user the user
     * @param refreshToken the refresh token
     * @param request the HTTP request
     * @return the created user session
     */
    @Transactional
    public UserSession createSession(User user, String refreshToken, HttpServletRequest request) {
        String deviceId = request.getHeader("X-Device-ID");
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(refreshTokenExpirationDays);
        
        // If a session already exists for this device, update it
        if (deviceId != null && !deviceId.isEmpty()) {
            Optional<UserSession> existingSession = userSessionRepository
                    .findByUserAndDeviceIdAndIsDeletedFalseAndIsActiveTrue(user, deviceId);
            
            if (existingSession.isPresent()) {
                return updateSession(existingSession.get(), refreshToken, ipAddress, userAgent, expiresAt);
            }
        }
        
        return createSession(user, refreshToken, deviceId, ipAddress, userAgent, expiresAt);
    }
    
    /**
     * Find a session by refresh token
     *
     * @param refreshToken the refresh token
     * @return the user session if found
     */
    public UserSession findByRefreshToken(String refreshToken) {
        return userSessionRepository.findByRefreshTokenAndIsDeletedFalseAndIsActiveTrue(refreshToken)
                .orElse(null);
    }
    
    /**
     * Find a session by user and device ID
     *
     * @param user the user
     * @param deviceId the device ID
     * @return the user session if found
     */
    public UserSession findByUserAndDeviceId(User user, String deviceId) {
        if (deviceId == null || deviceId.isEmpty()) {
            return null;
        }
        return userSessionRepository.findByUserAndDeviceIdAndIsDeletedFalseAndIsActiveTrue(user, deviceId)
                .orElse(null);
    }
    
    /**
     * Get all active sessions for a user
     *
     * @param user the user
     * @return list of active user sessions
     */
    public List<UserSession> getUserActiveSessions(User user) {
        return userSessionRepository.findByUserAndIsDeletedFalseAndIsActiveTrue(user);
    }
    
    /**
     * Invalidate a session by refresh token
     *
     * @param refreshToken the refresh token
     * @return true if the session was invalidated, false otherwise
     */
    @Transactional
    public boolean invalidateSession(String refreshToken) {
        int updated = userSessionRepository.deactivateByRefreshToken(refreshToken, LocalDateTime.now());
        return updated > 0;
    }
    
    /**
     * Invalidate a session by ID
     *
     * @param sessionId the session ID
     * @return true if the session was invalidated, false otherwise
     */
    @Transactional
    public boolean invalidateSessionById(UUID sessionId) {
        Optional<UserSession> session = userSessionRepository.findById(sessionId);
        if (session.isPresent()) {
            UserSession userSession = session.get();
            userSession.setActive(false);
            userSessionRepository.save(userSession);
            return true;
        }
        return false;
    }
    
    /**
     * Invalidate a session by user and device ID
     *
     * @param user the user
     * @param deviceId the device ID
     * @return true if the session was invalidated, false otherwise
     */
    @Transactional
    public boolean invalidateSessionByUserAndDeviceId(User user, String deviceId) {
        int updated = userSessionRepository.deactivateByUserAndDeviceId(user, deviceId, LocalDateTime.now());
        return updated > 0;
    }
    
    /**
     * Invalidate all sessions for a user
     *
     * @param user the user
     * @return number of invalidated sessions
     */
    @Transactional
    public int invalidateAllSessionsByUser(User user) {
        return userSessionRepository.deactivateAllUserSessions(user, LocalDateTime.now());
    }
    
    /**
     * Update session activity
     *
     * @param session the user session
     */
    @Transactional
    public void updateSessionActivity(UserSession session) {
        session.updateLastActivity();
        userSessionRepository.save(session);
    }
    
    /**
     * Update session with new refresh token and information
     *
     * @param session the user session
     * @param refreshToken the new refresh token
     * @param ipAddress the IP address
     * @param userAgent the user agent
     * @param expiresAt the expiration date
     * @return the updated session
     */
    @Transactional
    public UserSession updateSession(UserSession session, String refreshToken, String ipAddress, 
                                    String userAgent, LocalDateTime expiresAt) {
        session.setRefreshToken(refreshToken);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setLastActivity(LocalDateTime.now());
        session.setExpiresAt(expiresAt);
        session.setActive(true);
        return userSessionRepository.save(session);
    }
    
    /**
     * Scheduled task to clean up expired sessions
     */
    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void cleanupExpiredSessions() {
        int count = userSessionRepository.deactivateExpiredSessions(LocalDateTime.now());
        // if (count > 0) {
        //     log.info("Cleaned up {} expired user sessions", count);
        // }
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