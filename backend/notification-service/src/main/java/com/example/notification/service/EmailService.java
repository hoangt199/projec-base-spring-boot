package com.example.notification.service;

import com.example.common.exception.ResourceNotFoundException;
import com.example.notification.dto.EmailNotificationDto;
import com.example.notification.dto.SendEmailRequest;
import com.example.notification.mapper.EmailNotificationMapper;
import com.example.notification.model.EmailNotification;
import com.example.notification.model.EmailStatus;
import com.example.notification.repository.EmailNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailNotificationMapper emailNotificationMapper;
    private final JavaMailSender mailSender;

    @Transactional
    public EmailNotificationDto createEmailNotification(SendEmailRequest request) {
        EmailNotification emailNotification = emailNotificationMapper.toEntity(request);
        emailNotification = emailNotificationRepository.save(emailNotification);
        return emailNotificationMapper.toDto(emailNotification);
    }

    @Async
    @Transactional
    public void sendEmail(SendEmailRequest request) {
        EmailNotification emailNotification = emailNotificationMapper.toEntity(request);
        emailNotification = emailNotificationRepository.save(emailNotification);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailNotification.getRecipient());
            message.setSubject(emailNotification.getSubject());
            message.setText(emailNotification.getBody());
            
            mailSender.send(message);
            
            emailNotification.setStatus(EmailStatus.SENT);
            emailNotification.setSentAt(LocalDateTime.now());
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", emailNotification.getRecipient(), e.getMessage());
            emailNotification.setStatus(EmailStatus.FAILED);
            emailNotification.setErrorMessage(e.getMessage());
        }
        
        emailNotificationRepository.save(emailNotification);
    }

    @Transactional(readOnly = true)
    public List<EmailNotificationDto> getPendingEmails() {
        return emailNotificationRepository.findByStatus(EmailStatus.PENDING)
                .stream()
                .map(emailNotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmailNotificationDto> getFailedEmails() {
        return emailNotificationRepository.findByStatus(EmailStatus.FAILED)
                .stream()
                .map(emailNotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmailNotificationDto getEmailNotificationById(UUID id) {
        EmailNotification emailNotification = emailNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email notification not found with id: " + id));
        return emailNotificationMapper.toDto(emailNotification);
    }

    @Transactional
    public void retryFailedEmail(UUID id) {
        EmailNotification emailNotification = emailNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email notification not found with id: " + id));
        
        if (emailNotification.getStatus() != EmailStatus.FAILED) {
            throw new IllegalStateException("Can only retry failed emails");
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailNotification.getRecipient());
            message.setSubject(emailNotification.getSubject());
            message.setText(emailNotification.getBody());
            
            mailSender.send(message);
            
            emailNotification.setStatus(EmailStatus.SENT);
            emailNotification.setSentAt(LocalDateTime.now());
            emailNotification.setErrorMessage(null);
        } catch (MailException e) {
            log.error("Failed to retry sending email to {}: {}", emailNotification.getRecipient(), e.getMessage());
            emailNotification.setErrorMessage(e.getMessage());
        }
        
        emailNotificationRepository.save(emailNotification);
    }
}