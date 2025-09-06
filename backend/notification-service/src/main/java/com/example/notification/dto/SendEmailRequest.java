package com.example.notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    
    // Explicit getter for recipient
    public String getRecipient() {
        return recipient;
    }
    
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipient;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Body is required")
    private String body;
}