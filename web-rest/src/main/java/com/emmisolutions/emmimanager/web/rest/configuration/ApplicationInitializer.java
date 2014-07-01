package com.emmisolutions.emmimanager.web.rest.configuration;

import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_DEVELOPMENT;
import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_H2;

/**
 * Servlet 3.0 configuration.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.getEnvironment().setDefaultProfiles(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_H2);
        rootContext.setServletContext(servletContext);
        rootContext.register(RestConfiguration.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.addListener(new RequestContextListener());

        /*
            make sure Jersey/ Spring doesn't try to load the spring container
            @see org.glassfish.jersey.server.spring.SpringWebApplicationInitializer.onStartup
         */
        servletContext.setInitParameter("contextConfigLocation", "JERSEY_SPRING_IGNORE_DEFAULTS");
        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("webapi", new ServletContainer(new JerseyConfig(rootContext)));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/webapi/*");

    }

}
