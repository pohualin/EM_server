package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.emmisolutions.emmimanager.web.rest.admin.configuration.gzip.GZipServletFilter;
import com.emmisolutions.emmimanager.web.rest.client.configuration.ClientSecurityConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

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
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {

        // spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.getEnvironment().setDefaultProfiles(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_H2);
        rootContext.setServletContext(servletContext);
        rootContext.register(
                RestConfiguration.class,
                SecurityConfiguration.class,
                CasSecurityConfiguration.class,
                HateoasConfiguration.class,
                SwaggerConfiguration.class,
                ClientSecurityConfiguration.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.addListener(new RequestContextListener());

        // servlets and filters
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        initGzipFilter(servletContext, disps);
        initSecurity(servletContext, disps);
        initSpring(servletContext, disps, rootContext);

    }

    private void initSpring(ServletContext servletContext, EnumSet<DispatcherType> disps, AnnotationConfigWebApplicationContext rootContext) {
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private void initSecurity(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        FilterRegistration.Dynamic security = servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain"));
        security.addMappingForUrlPatterns(disps, true, "/*");
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
        compressingFilter.addMappingForUrlPatterns(disps, true, "/webapi-client/*");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
        compressingFilter.setAsyncSupported(true);
    }

}
