package com.example.user.service;

import com.example.common.exception.BusinessException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.user.dto.ForgotPasswordRequest;
import com.example.user.dto.ResetPasswordRequest;
import com.example.user.entity.PasswordResetToken;
import com.example.user.entity.User;
import com.example.user.repository.PasswordResetTokenRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for password reset functionality
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    @Value("${security.password-reset.token-expiry-minutes:30}")
    private int tokenExpiryMinutes;
    
    /**
     * Process a forgot password request
     *
     * @param request the forgot password request
     * @return true if the request was processed successfully
     */
    @Transactional
    public boolean processForgotPasswordRequest(ForgotPasswordRequest request) {
        // Find user by email
        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        // Invalidate any existing tokens
        tokenRepository.invalidateAllUserTokens(user);
        
        // Create new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenExpiryMinutes);
        
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiryDate)
                .isUsed(false)
                .build();
        
        tokenRepository.save(resetToken);
        
        // Send email with reset link
        String resetLink = "http://your-frontend-url/reset-password?token=" + token;
        String emailSubject = "Password Reset Request";
        String emailBody = "Hello " + user.getFirstName() + ",\n\n" +
                "You have requested to reset your password. Please click the link below to reset your password:\n\n" +
                resetLink + "\n\n" +
                "This link will expire in " + tokenExpiryMinutes + " minutes.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Regards,\n" +
                "Your Application Team";
        
        try {
            emailService.sendEmail(user.getEmail(), emailSubject, emailBody);
            return true;
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}", user.getEmail(), e);
            throw new BusinessException("Failed to send password reset email");
        }
    }
    
    /**
     * Reset a user's password
     *
     * @param request the reset password request
     * @return true if the password was reset successfully
     */
    @Transactional
    public boolean resetPassword(ResetPasswordRequest request) {
        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match");
        }
        
        // Find token
        PasswordResetToken token = tokenRepository.findByTokenAndIsDeletedFalseAndIsUsedFalse(request.getToken())
                .orElseThrow(() -> new BusinessException("Invalid or expired token"));
        
        // Check if token is valid
        if (!token.isValid()) {
            throw new BusinessException("Invalid or expired token");
        }
        
        // Update user password
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        
        // Mark token as used
        token.setUsed(true);
        tokenRepository.save(token);
        
        // Send confirmation email
        String emailSubject = "Password Reset Successful";
        String emailBody = "Hello " + user.getFirstName() + ",\n\n" +
                "Your password has been successfully reset.\n\n" +
                "If you did not perform this action, please contact our support team immediately.\n\n" +
                "Regards,\n" +
                "Your Application Team";
        
        try {
            emailService.sendEmail(user.getEmail(), emailSubject, emailBody);
        } catch (Exception e) {
            log.error("Failed to send password reset confirmation email to {}", user.getEmail(), e);
            // We don't throw an exception here as the password reset was successful
        }
        
        return true;
    }
}