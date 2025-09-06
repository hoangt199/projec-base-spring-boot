package com.example.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.refresh-token-ttl}")
    private long refreshTokenTtl;

    @Value("${redis.user-auth-cache-ttl}")
    private long userAuthCacheTtl;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";
    private static final String USER_AUTH_KEY_PREFIX = "user_auth:";

    // Refresh token operations
    public void saveRefreshToken(String username, String refreshToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenTtl, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String username) {
        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public void deleteRefreshToken(String username) {
        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        redisTemplate.delete(key);
    }

    // User authentication cache operations
    public void saveUserAuthentication(String username, String userAuthData) {
        String key = USER_AUTH_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, userAuthData, userAuthCacheTtl, TimeUnit.SECONDS);
    }

    public String getUserAuthentication(String username) {
        String key = USER_AUTH_KEY_PREFIX + username;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public void deleteUserAuthentication(String username) {
        String key = USER_AUTH_KEY_PREFIX + username;
        redisTemplate.delete(key);
    }
}