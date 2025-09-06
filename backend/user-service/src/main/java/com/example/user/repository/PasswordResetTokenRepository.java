package com.example.user.repository;

import com.example.user.entity.PasswordResetToken;
import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for password reset tokens
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    /**
     * Find a token by its value
     *
     * @param token the token value
     * @return the token if found
     */
    Optional<PasswordResetToken> findByTokenAndIsDeletedFalseAndIsUsedFalse(String token);

    /**
     * Find all valid tokens for a user
     *
     * @param user the user
     * @param now the current time
     * @return list of valid tokens
     */
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user = :user AND t.expiryDate > :now AND t.isUsed = false AND t.isDeleted = false")
    List<PasswordResetToken> findValidTokensByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    /**
     * Invalidate all tokens for a user
     *
     * @param user the user
     * @return number of tokens invalidated
     */
    @Query("UPDATE PasswordResetToken t SET t.isUsed = true WHERE t.user = :user AND t.isUsed = false AND t.isDeleted = false")
    int invalidateAllUserTokens(@Param("user") User user);
}