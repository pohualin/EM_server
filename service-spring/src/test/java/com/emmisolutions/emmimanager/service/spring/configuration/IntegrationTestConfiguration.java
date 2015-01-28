package com.emmisolutions.emmimanager.service.spring.configuration;

import com.icegreen.greenmail.spring.GreenMailBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;

/**
 * Any beans only necessary for testing
 */
@Configuration
public class IntegrationTestConfiguration {

    /**
     * An in memory email server
     *
     * @return GreenMailBean
     */
    @Bean
    public GreenMailBean greenMailBean() {
        return new GreenMailBean();
    }

    /**
     * configures the java mail sender to use the green mail
     * server
     *
     * @param javaMailSender to configure
     */
    @Resource
    public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
        javaMailSender.setHost(greenMailBean().getHostname());
        javaMailSender.setPort(greenMailBean().getGreenMail().getSmtp().getPort());
    }
}
