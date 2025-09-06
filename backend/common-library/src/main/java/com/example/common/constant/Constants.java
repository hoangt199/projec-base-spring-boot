package com.example.common.constant;

public class Constants {
    // JWT Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String USER_ID_KEY = "userId";
    
    // User Provider Constants
    public static final String PROVIDER_SYSTEM = "SYSTEM";
    public static final String PROVIDER_GOOGLE = "GOOGLE";
    public static final String PROVIDER_APPLE = "APPLE";
    
    // Redis Keys
    public static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";
    public static final String USER_AUTH_CACHE_PREFIX = "user_auth:";
    
    // Pagination Default Values
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    
    // Common Response Messages
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
    public static final String ERROR_MESSAGE = "An error occurred";
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized access";
    public static final String FORBIDDEN_MESSAGE = "Access forbidden";
    public static final String NOT_FOUND_MESSAGE = "Resource not found";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}