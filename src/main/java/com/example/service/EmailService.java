package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//this class handles sending verification emails
@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;//spring injects (Autowired) an instance of JavaMailSender that has been configured earlier with Gmail SMTP to send emails

    public void sendVerificationEmail(String to, //recipient email
                                      String subject, //email subject line
                                      String text //email body
                                       ) throws MessagingException {//this method is called when sending verification email
        //create the email
        MimeMessage message= emailSender.createMimeMessage(); //MimeMessage is the actual email object
        MimeMessageHelper helper=new MimeMessageHelper(message); //MimeMessageHelper help set fields (to, subject, body), true means it allows multipart messages (useful for HTML and attachments)

        //fill the email info:
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true); //true means it's HTML not plain text

        emailSender.send(message); //send the email
    }
}
/*
example:

emailService.sendVerificationEmail(
  "user@example.com",
  "Verify your email",
  "<p>Click <a href='http://example.com/verify?code=abc123'>here</a> to verify</p>"
);
The user would receive an email with a clickable link to verify their account.
         */