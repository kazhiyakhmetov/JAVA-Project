package com.example.hardlab5.next;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String name, String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Сообщение от " + name);
        message.setText(messageContent);
        message.setFrom("kazhiyakhmetov.nurbol@gmail.com");

        try {
            mailSender.send(message);
            System.out.println("Письмо успешно отправлено на " + toEmail);
        } catch (MailException e) {
            System.err.println("Ошибка при отправке письма: " + e.getMessage());
        }
    }
}