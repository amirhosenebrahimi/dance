package com.erymanthian.dance.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {
    private final MailSender mailSender;

    public void sendMessage(String to, String code) {
        var message = new SimpleMailMessage();
        message.setTo(to);
        message.setText("You're Code: %s".formatted(code));
        message.setSubject("Verify Email Address");
        mailSender.send(message);
    }
}
