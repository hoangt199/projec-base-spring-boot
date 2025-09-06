package com.example.user.service;

import com.example.common.exception.BusinessException;
import com.example.common.exception.UnauthorizedException;
import com.example.common.security.JwtUtils;
import com.example.user.dto.AuthRequest;
import com.example.user.dto.AuthResponse;
import com.example.user.dto.RefreshTokenRequest;
import com.example.user.dto.RegisterRequest;
import com.example.user.dto.UserDto;
import com.example.user.entity.Module;
import com.example.user.entity.RoleEntity;
import com.example.user.entity.UserSession;
import com.example.user.repository.ModuleRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.entity.User;
import com.example.user.mapper.UserMappInf;
import com.example.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final UserMappInf userMapper;
    private final PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final UserSessionService userSessionService;
    private final LoginAttemptService loginAttemptService;
    private final UserModuleRoleService userModuleRoleService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        // Create new user
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Assign default USER role
        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BusinessException("Default user role not found"));        
        user.setRole(userRole);
        
        User savedUser = userRepository.save(user);
        
        // Assign modules to user if provided in the request
        if (request.getModuleIds() != null && !request.getModuleIds().isEmpty()) {
            for (UUID moduleId : request.getModuleIds()) {
                Module module = moduleRepository.findById(moduleId)
                        .orElseThrow(() -> new BusinessException("Module not found with id: " + moduleId));
                
                // Assign default USER role for each module
                userModuleRoleService.assignUserToModuleWithRole(savedUser.getId(), moduleId, userRole.getId());
            }
        }
        UserDto userDto = userMapper.toDto(savedUser);

        // Generate tokens
        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        // Save refresh token to Redis
        redisService.saveRefreshToken(user.getUsername(), refreshToken);

        // Cache user authentication
        cacheUserAuthentication(user);
        
        // Get request information
        HttpServletRequest request = getCurrentRequest();
        String deviceId = UUID.randomUUID().toString(); // Default device ID for new registration
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        // Create user session
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7); // 7 days for refresh token
        UserSession session = userSessionService.createSession(savedUser, refreshToken, deviceId, ipAddress, userAgent, expiresAt);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userDto)
                .deviceId(deviceId)
                .expiresAt(expiresAt)
                .sessionId(session.getId().toString())
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Check if account is locked due to too many failed attempts
        if (loginAttemptService.isAccountLocked(request.getUsername())) {
            long remainingSeconds = loginAttemptService.getRemainingLockTime(request.getUsername());
            throw new BusinessException("Account is temporarily locked. Please try again in " + remainingSeconds + " seconds.");
        }
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        UserDto userDto = userMapper.toDto(user);

        // Reset failed login attempts on successful login
        loginAttemptService.resetFailedAttempts(request.getUsername());
        
        // Generate tokens
        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        // Save refresh token to Redis
        redisService.saveRefreshToken(user.getUsername(), refreshToken);

        // Cache user authentication
        cacheUserAuthentication(user);
        
        // Get request information
        HttpServletRequest httpRequest = getCurrentRequest();
        String deviceId = request.getDeviceId();
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString(); // Generate a default device ID if not provided
        }
        String ipAddress = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        // Check if a session already exists for this device
        UserSession existingSession = userSessionService.findByUserAndDeviceId(user, deviceId);
        UserSession session;
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7); // 7 days for refresh token
        
        if (existingSession != null) {
            // Update existing session
            session = userSessionService.updateSession(existingSession, refreshToken, ipAddress, userAgent, expiresAt);
        } else {
            // Create new session
            session = userSessionService.createSession(user, refreshToken, deviceId, ipAddress, userAgent, expiresAt);
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userDto)
                .deviceId(deviceId)
                .expiresAt(expiresAt)
                .sessionId(session.getId().toString())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtUtils.extractUsername(refreshToken);

        if (username == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        // Validate refresh token from Redis
        String storedRefreshToken = redisService.getRefreshToken(username);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new UnauthorizedException("Refresh token is expired or invalid");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
                
        // Find the session associated with this refresh token
        UserSession session = userSessionService.findByRefreshToken(refreshToken);
        if (session == null) {
            throw new UnauthorizedException("Session not found or expired");
        }
        
        if (!session.isActive()) {
            throw new UnauthorizedException("Session is no longer active");
        }

        // Generate new tokens
        String newAccessToken = jwtUtils.generateToken(user);
        String newRefreshToken = jwtUtils.generateRefreshToken(user);

        // Update refresh token in Redis
        redisService.saveRefreshToken(username, newRefreshToken);
        
        // Update session with new refresh token
        HttpServletRequest httpRequest = getCurrentRequest();
        String ipAddress = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7); // 7 days for refresh token
        
        session = userSessionService.updateSession(session, newRefreshToken, ipAddress, userAgent, expiresAt);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .user(userMapper.toDto(user))
                .deviceId(session.getDeviceId())
                .expiresAt(expiresAt)
                .sessionId(session.getId().toString())
                .build();
    }

    public void logout(String username) {
        // Get current request to extract device information
        HttpServletRequest request = getCurrentRequest();
        String deviceId = request.getHeader("X-Device-Id");
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        if (deviceId != null && !deviceId.isEmpty()) {
            // Logout from specific device
            userSessionService.invalidateSessionByUserAndDeviceId(user, deviceId);
        } else {
            // Logout from all devices
            userSessionService.invalidateAllSessionsByUser(user);
        }
        
        // Remove refresh token and user authentication from Redis
        redisService.deleteRefreshToken(username);
        redisService.deleteUserAuthentication(username);
    }
    
    /**
     * Logout a user from a specific session
     * 
     * @param username the username
     * @param sessionId the session ID
     */
    public void logoutBySessionId(String username, String sessionId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        try {
            UUID sessionUuid = UUID.fromString(sessionId);
            userSessionService.invalidateSessionById(sessionUuid);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid session ID format");
        }
    }
    
    /**
     * Logout a user from a specific device
     * 
     * @param username the username
     * @param deviceId the device ID
     */
    public void logoutByDeviceId(String username, String deviceId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        userSessionService.invalidateSessionByUserAndDeviceId(user, deviceId);
    }

    private void cacheUserAuthentication(User user) {
        try {
            String userJson = objectMapper.writeValueAsString(userMapper.toDto(user));
            redisService.saveUserAuthentication(user.getUsername(), userJson);
        } catch (JsonProcessingException e) {
            // Log error but continue - caching is not critical
        }
    }
    
    /**
     * Get the current HTTP request
     * 
     * @return the current HTTP request
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        throw new BusinessException("No active request context found");
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
            // In case of multiple proxies, the first IP is the original client
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}