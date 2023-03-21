package com.example.piramidadjii.baseModule;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class MailSenderService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String recipient,
                          String subject,
                          String text,
                          String attachment) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(text, true); // true indicates HTML content

        FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
        helper.addAttachment(Objects.requireNonNull(fileSystemResource.getFilename()), fileSystemResource);

        mailSender.send(message);
    }

    public void sendEmailWithoutAttachment(String recipient, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(text, true); // true indicates HTML content

        mailSender.send(message);
    }
}
