package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/* this setup is required to:
1. send verification links
2. notify users via email
3. integrate with Spring's JavaMailSender for EmailService
 */
@Configuration //marks this class as a Spring configuration class used to define beans
public class EmailConfiguration {
    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    /*
    sets up JavaMailSender for Gmail SMTP:
    - enables sending verification emails
    - uses gmail SMTP with TLS and authentication
    - helps improve security and trust in the system
     */
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); //spring's email sender implementation
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties props=mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable TLS encryption
        props.put("mail.debug", "true"); //enable logging for debugging
        return mailSender;
    }
}