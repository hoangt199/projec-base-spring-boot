package com.example.gateway.service;

import com.example.common.constant.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String userId, String refreshToken, long ttl) {
        String key = Constants.REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, ttl, TimeUnit.SECONDS);
        log.info("Saved refresh token for user: {}", userId);
    }

    public String getRefreshToken(String userId) {
        String key = Constants.REFRESH_TOKEN_KEY_PREFIX + userId;
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    public void deleteRefreshToken(String userId) {
        String key = Constants.REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
        log.info("Deleted refresh token for user: {}", userId);
    }

    public void saveUserAuthCache(String userId, String authData, long ttl) {
        String key = Constants.USER_AUTH_CACHE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, authData, ttl, TimeUnit.SECONDS);
    }

    public String getUserAuthCache(String userId) {
        String key = Constants.USER_AUTH_CACHE_PREFIX + userId;
        Object authData = redisTemplate.opsForValue().get(key);
        return authData != null ? authData.toString() : null;
    }

    public void deleteUserAuthCache(String userId) {
        String key = Constants.USER_AUTH_CACHE_PREFIX + userId;
        redisTemplate.delete(key);
    }
}