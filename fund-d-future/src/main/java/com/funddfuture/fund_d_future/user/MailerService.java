package com.funddfuture.fund_d_future.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailerService {

    @Autowired
    private final JavaMailSender mailSender;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public MailerService(JavaMailSender mailSender) {

        this.mailSender = mailSender;

    }

    public void sendPasswordResetEmail(String email, String resetToken) {
        String subject = "Password Reset Request";
        String content = "To reset your password, click the link below:\n" + resetPasswordUrl + resetToken;
        System.out.printf("Helllooo DAMMY %s, %s, %s%n", email, mailSender, emailFrom);        SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(emailFrom);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content);

        mailSender.send(message);
    }
}
