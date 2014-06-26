package com.emmisolutions.emmimanager.web.rest.configuration;

import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Servlet 3.0 configuration.
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        ServletRegistration.Dynamic dispatcher =
                container.addServlet("webapi", new ServletContainer(new JerseyConfig()));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/webapi/*");
    }

}
