package com.example.pos_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Async // <--- BURASI: Bu metod artık ana akışı durdurmadan arka planda çalışır.
    public void sendResetCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Şifre Sıfırlama Kodu");
        message.setText("Merhaba,\n\nŞifrenizi sıfırlamak için kullanacağınız kod: " + code +
                "\nBu kod 15 dakika geçerlidir.\n\nEğer bu talebi siz yapmadıysanız lütfen dikkate almayın.");

        mailSender.send(message);
    }
}