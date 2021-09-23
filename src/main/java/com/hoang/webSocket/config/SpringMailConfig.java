package com.hoang.webSocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SpringMailConfig {

    @Value("#{'${spring.mail.username}'.trim()}")
    private String email;

    @Value("#{'${spring.mail.password}'.trim()}")
    private String password;

    @Value("#{'${spring.mail.host}'.trim()}")
    private String mailHost;

    @Value("#{'${spring.mail.port}'.trim()}")
    private int mailPort;

    @Value("#{'${spring.mail.debug}'.trim()}")
    private boolean mailDebug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", mailDebug);

        return mailSender;
    }
}
