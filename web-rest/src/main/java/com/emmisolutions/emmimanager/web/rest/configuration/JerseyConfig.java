package com.emmisolutions.emmimanager.web.rest.configuration;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import java.util.logging.Logger;

public class JerseyConfig extends ResourceConfig {

    Logger logger = Logger.getLogger("com.emmisolutions.emmimanager.web.rest.endpoint");

    public JerseyConfig() {
        register(RequestContextFilter.class);
        packages("com.emmisolutions.emmimanager.web.rest.jax_rs");
        register(DeclarativeLinkingFeature.class);
        LoggingFilter loggingFilter = new LoggingFilter(logger, true);
        register(loggingFilter);
        register(JacksonObjectMapper.class);
        register(JacksonFeature.class);
    }
}
