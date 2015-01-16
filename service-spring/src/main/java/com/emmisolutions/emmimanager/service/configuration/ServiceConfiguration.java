package com.emmisolutions.emmimanager.service.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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

}
