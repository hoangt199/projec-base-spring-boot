package com.example.user.repository;

import com.example.user.entity.LoginFailureAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginFailureAttemptRepository extends JpaRepository<LoginFailureAttempt, Long> {

    /**
     * Find all login failure attempts for a specific username within a time window
     *
     * @param username the username
     * @param startTime the start time of the window
     * @return list of login failure attempts
     */
    @Query("SELECT lfa FROM LoginFailureAttempt lfa WHERE lfa.username = :username AND lfa.attemptTime >= :startTime AND lfa.isDeleted = false")
    List<LoginFailureAttempt> findRecentAttemptsByUsername(
            @Param("username") String username,
            @Param("startTime") LocalDateTime startTime);

    /**
     * Count login failure attempts for a specific username within a time window
     *
     * @param username the username
     * @param startTime the start time of the window
     * @return count of login failure attempts
     */
    @Query("SELECT COUNT(lfa) FROM LoginFailureAttempt lfa WHERE lfa.username = :username AND lfa.attemptTime >= :startTime AND lfa.isDeleted = false")
    int countRecentAttemptsByUsername(
            @Param("username") String username,
            @Param("startTime") LocalDateTime startTime);

    /**
     * Find all login failure attempts for a specific username and IP address within a time window
     *
     * @param username the username
     * @param ipAddress the IP address
     * @param startTime the start time of the window
     * @return list of login failure attempts
     */
    @Query("SELECT lfa FROM LoginFailureAttempt lfa WHERE lfa.username = :username AND lfa.ipAddress = :ipAddress AND lfa.attemptTime >= :startTime AND lfa.isDeleted = false")
    List<LoginFailureAttempt> findRecentAttemptsByUsernameAndIpAddress(
            @Param("username") String username,
            @Param("ipAddress") String ipAddress,
            @Param("startTime") LocalDateTime startTime);
}