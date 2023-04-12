package com.erymanthian.dance.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService implements CommunicationService {
    private final MailSender mailSender;

    public void sendMessage(String email, String code) {
        var message = new SimpleMailMessage();
        message.setTo(email);
        message.setText("You're Code: %s".formatted(code));
        message.setSubject("Verify Email Address");
        mailSender.send(message);
    }
}
