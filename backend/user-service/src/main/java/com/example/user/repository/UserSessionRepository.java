package com.example.user.repository;

import com.example.user.entity.User;
import com.example.user.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    /**
     * Find a session by refresh token
     *
     * @param refreshToken the refresh token
     * @return the user session if found
     */
    Optional<UserSession> findByRefreshTokenAndIsDeletedFalseAndIsActiveTrue(String refreshToken);

    /**
     * Find all active sessions for a user
     *
     * @param user the user
     * @return list of active user sessions
     */
    List<UserSession> findByUserAndIsDeletedFalseAndIsActiveTrue(User user);

    /**
     * Find a session by user and device ID
     *
     * @param user the user
     * @param deviceId the device ID
     * @return the user session if found
     */
    Optional<UserSession> findByUserAndDeviceIdAndIsDeletedFalseAndIsActiveTrue(User user, String deviceId);

    /**
     * Deactivate all sessions for a user
     *
     * @param user the user
     * @param currentTime the current time
     * @return number of affected rows
     */
    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false, us.updatedAt = :currentTime WHERE us.user = :user AND us.isActive = true AND us.isDeleted = false")
    int deactivateAllUserSessions(@Param("user") User user, @Param("currentTime") LocalDateTime currentTime);

    /**
     * Deactivate a session by refresh token
     *
     * @param refreshToken the refresh token
     * @param currentTime the current time
     * @return number of affected rows
     */
    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false, us.updatedAt = :currentTime WHERE us.refreshToken = :refreshToken AND us.isActive = true AND us.isDeleted = false")
    int deactivateByRefreshToken(@Param("refreshToken") String refreshToken, @Param("currentTime") LocalDateTime currentTime);
    
    /**
     * Deactivate a session by user and device ID
     *
     * @param user the user
     * @param deviceId the device ID
     * @param currentTime the current time
     * @return number of affected rows
     */
    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false, us.updatedAt = :currentTime WHERE us.user = :user AND us.deviceId = :deviceId AND us.isActive = true AND us.isDeleted = false")
    int deactivateByUserAndDeviceId(@Param("user") User user, @Param("deviceId") String deviceId, @Param("currentTime") LocalDateTime currentTime);

    /**
     * Clean up expired sessions
     *
     * @param currentTime the current time
     * @return number of affected rows
     */
    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false, us.updatedAt = :currentTime WHERE us.expiresAt < :currentTime AND us.isActive = true AND us.isDeleted = false")
    int deactivateExpiredSessions(@Param("currentTime") LocalDateTime currentTime);
}