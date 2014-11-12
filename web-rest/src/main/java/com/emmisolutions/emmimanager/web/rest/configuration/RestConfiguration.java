package com.emmisolutions.emmimanager.web.rest.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import cz.jirutka.spring.exhandler.RestHandlerExceptionResolver;
import cz.jirutka.spring.exhandler.support.HttpMessageConverterUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.geo.GeoModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring container configuration.
 */
@Configuration
@ComponentScan(basePackages = {
    "com.emmisolutions.emmimanager.service.configuration",
    "com.emmisolutions.emmimanager.web.rest.resource",
    "com.emmisolutions.emmimanager.web.rest.model"
})
public class RestConfiguration extends DelegatingWebMvcConfiguration {

    /**
     * Override the defaults because they aren't what we need. Only enable JSON and XML
     *
     * @param converters to add to
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new JsonJacksonConverter());
        converters.add(new XmlJacksonConverter());
    }

    @Override
    protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(exceptionHandlerExceptionResolver()); // resolves @ExceptionHandler
        resolvers.add(restExceptionResolver());
    }

    @Bean
    public RestHandlerExceptionResolver restExceptionResolver() {
        return RestHandlerExceptionResolver.builder()
            .messageSource(httpErrorMessageSource())
            .defaultContentType(MediaType.APPLICATION_JSON)
            .addErrorMessageHandler(EmptyResultDataAccessException.class, HttpStatus.NOT_FOUND)
            .addErrorMessageHandler(OptimisticLockingFailureException.class, HttpStatus.CONFLICT)
            .addErrorMessageHandler(AccessDeniedException.class, HttpStatus.FORBIDDEN)
            .addErrorMessageHandler(AuthorizationServiceException.class, HttpStatus.INTERNAL_SERVER_ERROR)
            .build();
    }

    @Bean
    public MessageSource httpErrorMessageSource() {
        ReloadableResourceBundleMessageSource m = new ReloadableResourceBundleMessageSource();
        m.setBasename("classpath:/errors/messages");
        m.setDefaultEncoding("UTF-8");
        return m;
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(HttpMessageConverterUtils.getDefaultHttpMessageConverters());
        return resolver;
    }

    private void enableJacksonFeatures(ObjectMapper mapper) {
        // make it pretty
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // allow random properties to come in
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ISO8601 Formatted Dates
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // don't write null values in JSON
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // Joda Dates, only support ISO8601
        mapper.registerModule(new JodaModule());
        // JAXB as well as Jackson Annotations
        mapper.registerModule(new JaxbAnnotationModule());
        // Hibernate (lazy loaded entities ==> null)
        mapper.registerModule(new Hibernate4Module());
        // serialize as XML as well as JSON
        mapper.registerModule(new JacksonXmlModule());
        // spatial mappings via spring-data
        mapper.registerModule(new GeoModule());
    }

    private class XmlJacksonConverter extends MappingJackson2HttpMessageConverter {
        /**
         * Constructor to add XML mapper
         */
        public XmlJacksonConverter() {
            List<MediaType> supportedTypes = new ArrayList<>();
            supportedTypes.add(MediaType.APPLICATION_XML);
            supportedTypes.add(MediaType.TEXT_XML);
            supportedTypes.add(new MediaType("application", "*+xml"));
            setSupportedMediaTypes(supportedTypes);
            ObjectMapper xmlMapper = new XmlMapper();
            enableJacksonFeatures(xmlMapper);
            setObjectMapper(xmlMapper);
        }
    }

    private class JsonJacksonConverter extends MappingJackson2HttpMessageConverter {
        /**
         * Add JSON converter
         */
        public JsonJacksonConverter() {
            enableJacksonFeatures(getObjectMapper());
        }
    }
}
