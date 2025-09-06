package com.example.user.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Entity to store login failure attempts
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_failure_attempts")
public class LoginFailureAttempt extends BaseModel {
    
    // Sử dụng annotation @SuperBuilder của Lombok để tạo builder
    
    // Explicit getter and setter methods
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }
    
    public void setAttemptTime(LocalDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "attempt_time", nullable = false)
    private LocalDateTime attemptTime;
    
    @Column(name = "device_id")
    private String deviceId;
    
    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        attemptTime = LocalDateTime.now();
    }
}