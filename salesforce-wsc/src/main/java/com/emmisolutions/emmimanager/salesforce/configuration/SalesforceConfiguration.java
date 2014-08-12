package com.emmisolutions.emmimanager.salesforce.configuration;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.salesforce.wsc"
})
public class SalesforceConfiguration {


}
