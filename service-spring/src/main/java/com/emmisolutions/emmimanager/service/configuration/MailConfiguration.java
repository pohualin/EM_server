package com.emmisolutions.emmimanager.service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Email Configuration
 */
@Configuration
public class MailConfiguration {

    private static final String PROP_SMTP_AUTH = "mail.smtp.auth";
    private static final String PROP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String PROP_TRANSPORT_PROTO = "mail.transport.protocol";

    @Value("${mail.host:127.0.0.1}")
    private String host;

    @Value("${mail.port:25}")
    private Integer port;

    @Value("${mail.user:}")
    private String user;

    @Value("${mail.password:}")
    private String password;

    @Value("${mail.protocol:smtp}")
    private String protocol;

    @Value("${mail.tls:false}")
    private Boolean tls;

    @Value("${mail.auth:false}")
    private Boolean auth;

    /**
     * Create the JavaMailSender bean with the configuration set
     *
     * @return the configured mail sender
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(password);
        Properties sendProperties = new Properties();
        sendProperties.setProperty(PROP_SMTP_AUTH, auth.toString());
        sendProperties.setProperty(PROP_STARTTLS, tls.toString());
        sendProperties.setProperty(PROP_TRANSPORT_PROTO, protocol);
        sender.setJavaMailProperties(sendProperties);
        return sender;
    }
}
