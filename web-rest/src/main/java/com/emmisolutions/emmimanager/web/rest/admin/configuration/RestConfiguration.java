package com.emmisolutions.emmimanager.web.rest.admin.configuration;

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
import cz.jirutka.spring.exhandler.handlers.AbstractRestExceptionHandler;
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
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring container configuration.
 */
@Configuration
@ComponentScan(basePackages = {
    "com.emmisolutions.emmimanager.service.configuration",
        "com.emmisolutions.emmimanager.web.rest.admin.resource",
        "com.emmisolutions.emmimanager.web.rest.admin.model",
        "com.emmisolutions.emmimanager.web.rest.client.resource",
        "com.emmisolutions.emmimanager.web.rest.client.model"
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

    /**
     * Handles REST Exception serialization
     *
     * @return the resolver
     */
    @Bean
    public RestHandlerExceptionResolver restExceptionResolver() {
        return RestHandlerExceptionResolver.builder()
            .messageSource(httpErrorMessageSource())
            .defaultContentType(MediaType.APPLICATION_JSON)
            .addErrorMessageHandler(EmptyResultDataAccessException.class, HttpStatus.NOT_FOUND)
            .addErrorMessageHandler(OptimisticLockingFailureException.class, HttpStatus.CONFLICT)
            .addErrorMessageHandler(AccessDeniedException.class, HttpStatus.FORBIDDEN)
            .addHandler(Exception.class, new UncaughtExceptionRestExceptionHandler())
            .build();
    }

    /**
     * Where to resolve error messages
     *
     * @return message source
     */
    @Bean
    public MessageSource httpErrorMessageSource() {
        ReloadableResourceBundleMessageSource m = new ReloadableResourceBundleMessageSource();
        m.setBasename("classpath:/errors/messages");
        m.setDefaultEncoding("UTF-8");
        return m;
    }

    /**
     * Exception handler resolver
     * @return resolver
     */
    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(getMessageConverters());
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

    /**
     * This class writes the stack trace to the error message details
     */
    @SuppressWarnings("all")
    private class UncaughtExceptionRestExceptionHandler extends AbstractRestExceptionHandler<Exception, ErrorMessage> {
        /**
         * Constructor which sets 500 error
         */
        private UncaughtExceptionRestExceptionHandler() {
            super(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Override
        public ErrorMessage createBody(Exception ex, HttpServletRequest req) {
            ErrorMessage m = new ErrorMessage();
            m.setTitle("Uncaught Exception");
            m.setStatus(getStatus());
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            m.setDetail(sw.toString());
            return m;
        }
    }

    /**
     * Patch to the ErrorMessage because it has dependencies on jars which don't exist in our project.
     * This class removes the compile warnings.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlRootElement(name = "problem")
    public static class ErrorMessage implements Serializable {

        private static final long serialVersionUID = 1L;

        private URI type;

        private String title;

        private Integer status;

        private String detail;

        private URI instance;

        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setStatus(HttpStatus status) {
            this.setStatus(status.value());
        }

        /**
         * An absolute URI that identifies the problem type. When dereferenced, it
         * SHOULD provide human-readable documentation for the problem type (e.g.,
         * using HTML). When this member is not present, its value is assumed to
         * be "about:blank".
         */
        public URI getType() {
            return type;
        }

        public void setType(URI type) {
            this.type = type;
        }

        /**
         * A short, human-readable summary of the problem type. It SHOULD NOT
         * change from occurrence to occurrence of the problem, except for purposes
         * of localization.
         */
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * The HTTP status code generated by the origin server for this occurrence
         * of the problem.
         */
        public Integer getStatus() {
            return status;
        }

        /**
         * An human readable explanation specific to this occurrence of the
         * problem.
         */
        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        /**
         * An absolute URI that identifies the specific occurrence of the problem.
         * It may or may not yield further information if dereferenced.
         */
        public URI getInstance() {
            return instance;
        }

        public void setInstance(URI instance) {
            this.instance = instance;
        }
    }
}
