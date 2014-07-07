package com.emmisolutions.emmimanager.web.rest.configuration;

import com.emmisolutions.emmimanager.web.rest.configuration.gzip.GZipServletFilter;
import com.thetransactioncompany.cors.CORSFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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

        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

        // setup CORS, the defaults are good in the filter
        FilterRegistration.Dynamic corsFilter =
                servletContext.addFilter("crossOriginResourceSharingFilter", CORSFilter.class);
        corsFilter.setInitParameter("cors.allowGenericHttpRequests", "true");
        corsFilter.setInitParameter("cors.allowOrigin", "*");
        corsFilter.setInitParameter("cors.allowSubdomains", "true");
        corsFilter.setInitParameter("cors.supportedMethods", "GET, POST, HEAD, OPTIONS, PUT, DELETE");
        corsFilter.setInitParameter("cors.supportedHeaders", "*");
        corsFilter.setInitParameter("cors.exposedHeaders", "");
        corsFilter.setInitParameter("cors.supportsCredentials", "true");
        corsFilter.setInitParameter("cors.maxAge", "-1");
        corsFilter.setInitParameter("cors.tagRequests", "false");
        corsFilter.addMappingForUrlPatterns(disps, false, "/*");

        /*
            make sure Jersey/ Spring doesn't try to load the spring container
            @see org.glassfish.jersey.server.spring.SpringWebApplicationInitializer.onStartup
         */
        servletContext.setInitParameter("contextConfigLocation", "JERSEY_SPRING_IGNORE_DEFAULTS");
        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("webapi", new ServletContainer(new JerseyConfig()));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/webapi/*");

        initGzipFilter(servletContext, disps);

    }

    private void initGzipFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {

        FilterRegistration.Dynamic compressingFilter = servletContext.addFilter("gzipFilter", new GZipServletFilter());
        Map<String, String> parameters = new HashMap<>();
        compressingFilter.setInitParameters(parameters);
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/webapi/*");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");

        compressingFilter.setAsyncSupported(true);
    }

}
