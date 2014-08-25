package com.emmisolutions.emmimanager.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The service layer spring configuration
 */
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.salesforce.configuration",
        "com.emmisolutions.emmimanager.persistence.configuration",

        "com.emmisolutions.emmimanager.service"})
public class ServiceConfiguration {

    /**
     * A password encoder
     *
     * @return the encoder we use
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
