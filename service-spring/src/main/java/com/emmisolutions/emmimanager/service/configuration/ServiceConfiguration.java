package com.emmisolutions.emmimanager.service.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.service.spring",
        "com.emmisolutions.emmimanager.service.logging",
        "com.emmisolutions.emmimanager.persistence.configuration"})
public class ServiceConfiguration {

}
