package org.blogapp.services.Email;

import lombok.NoArgsConstructor;
import org.blogapp.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

@Service
@NoArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage (final Mail mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, mail.isMultipart(), "utf-8");

        message.setContent(mail.getContent(), "text/html");
        helper.setTo(mail.getTo());
        helper.setSubject("Confirm registration");
        helper.setFrom(mail.getFrom());

        mailSender.send(message);
    }
}
