package com.emmisolutions.emmimanager.service.configuration;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;

/**
 * Enable Thymeleaf as a rendering engine for email templates
 */
@Configuration
public class ThymeleafConfiguration {

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    /**
     * Enable message resolution for mail messages.
     *
     * @return a message source
     */
    @Bean
    public MessageSource emailMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/mails/messages/messages");
        messageSource.setDefaultEncoding(CharEncoding.UTF_8);
        return messageSource;
    }

    /**
     * Resolve templates via the classpath
     *
     * @return a spring resource template resolver
     */
    @Bean
    public TemplateResolver springThymeleafTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/mails/templates/");
        resolver.setSuffix(".html");
        resolver.setOrder(1);
        return resolver;
    }

    private final static String DB_RESOLVER_PREFIX = "db:";

    /**
     * Makes a new Thymeleaf TemplateResolver that resolves all db:type values
     * that uses our own persistence layer to load it from the database
     *
     * @return a new TemplateResolver
     */
    @Bean
    public TemplateResolver dbTemplateResolver() {

        return new TemplateResolver() {
            @Override
            protected String computeResourceName(TemplateProcessingParameters params) {
                String templateName = params.getTemplateName();
                return templateName.substring(DB_RESOLVER_PREFIX.length());
            }

            {
                setOrder(2);
                setResolvablePatterns(new HashSet<String>() {{
                    add(DB_RESOLVER_PREFIX + "*");
                }});
                setResourceResolver(new IResourceResolver() {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {
                        EmailTemplate template = emailTemplatePersistence.find(EmailTemplateType.valueOf(resourceName));
                        if (template != null && StringUtils.isNotBlank(template.getContent())) {
                            return new ByteArrayInputStream(template.getContent().getBytes());
                        }
                        return null;
                    }
                });
            }
        };
    }

    /**
     * Creates TemplateEngine using the resolvers configured in this class
     *
     * @return a SpringTemplateEngine
     */
    @Bean
    public TemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolvers(new HashSet<ITemplateResolver>() {{
            add(springThymeleafTemplateResolver());
            add(dbTemplateResolver());
        }});
        return engine;
    }
}
