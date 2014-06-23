package com.emmisolutions.emmimanager.rest.config;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;


public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        packages("com.emmisolutions.emmimanager.rest.endpoint");
        register(DeclarativeLinkingFeature.class);
//        register(LoggingFilter.class); enable if you want to see the HTTP requests themselves
    }
}
