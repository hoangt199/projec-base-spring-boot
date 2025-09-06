package com.example.user.security.filter;

import com.example.common.response.ErrorResponse;
import com.example.user.service.LoginAttemptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filter to handle login attempts and account locking
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptFilter extends OncePerRequestFilter {
    // Biến log được tạo tự động bởi annotation @Slf4j của Lombok

    private final LoginAttemptService loginAttemptService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Only apply to login endpoint with POST method
        if ("/api/auth/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            // Extract username from request
            String username = extractUsername(request);
            
            if (username != null && !username.isEmpty()) {
                // Check if account is locked
                if (loginAttemptService.isAccountLocked(username)) {
                    long remainingSeconds = loginAttemptService.getRemainingLockTime(username);
                    handleAccountLocked(response, username, remainingSeconds);
                    return;
                }
            }
            
            // Wrap the request to allow reading the input stream multiple times
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            
            try {
                filterChain.doFilter(cachedRequest, response);
                
                // If we get here and there's no authentication in the context, it might be a failed login
                if (SecurityContextHolder.getContext().getAuthentication() == null && username != null) {
                    // Check response status to determine if login failed
                    if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                        loginAttemptService.recordFailedAttempt(username, request);
                        // log.debug("Recorded failed login attempt for user: {}", username);
                    }
                }
            } catch (AuthenticationException ex) {
                if (username != null) {
                    if (ex instanceof BadCredentialsException) {
                        loginAttemptService.recordFailedAttempt(username, request);
                        // log.debug("Recorded failed login attempt for user: {}", username);
                    } else if (ex instanceof LockedException) {
                        // Account already locked by Spring Security
                        // log.debug("Account already locked for user: {}", username);
                    }
                }
                throw ex;
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Extract username from login request
     *
     * @param request the HTTP request
     * @return the username or null if not found
     */
    private String extractUsername(HttpServletRequest request) {
        try {
            if (request instanceof CachedBodyHttpServletRequest) {
                String body = new String(((CachedBodyHttpServletRequest) request).getContentAsByteArray());
                LoginRequest loginRequest = objectMapper.readValue(body, LoginRequest.class);
                return loginRequest.getUsername();
            } else {
                // For the first pass, we can't read the input stream yet
                return request.getParameter("username");
            }
        } catch (Exception e) {
            // log.error("Error extracting username from request", e);
            return null;
        }
    }

    /**
     * Handle locked account response
     *
     * @param response the HTTP response
     * @param username the username
     * @param remainingSeconds remaining lock time in seconds
     * @throws IOException if an I/O error occurs
     */
    private void handleAccountLocked(HttpServletResponse response, String username, long remainingSeconds) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()),
                "Account temporarily locked",
                new ArrayList<>()
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * DTO for login request
     */
    private static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}