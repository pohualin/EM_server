package com.emmisolutions.emmimanager.salesforce.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_DEVELOPMENT;
import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_TEST;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.salesforce.wsc"
})
public class SalesForceConfiguration {

    @Bean(name = "salesForceProperties")
    public static PropertySourcesPlaceholderConfigurer propertyResolver(ConfigurableEnvironment environment) throws IOException {
        // allow for JNDI properties to be resolved
        JndiPropertySource jndiPropertySource = new JndiPropertySource("salesForceJndiProperties");
        environment.getPropertySources().addLast(jndiPropertySource);

        // for development/test use the embedded properties file
        if (environment.acceptsProfiles(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_TEST)) {
            ResourcePropertySource resourcePropertySource = new ResourcePropertySource("sf", "classpath:salesforce.properties");
            environment.getPropertySources().addLast(resourcePropertySource);
        }

        PropertySourcesPlaceholderConfigurer ret = new PropertySourcesPlaceholderConfigurer();
        ret.setPropertySources(environment.getPropertySources());
        return ret;
    }


}
