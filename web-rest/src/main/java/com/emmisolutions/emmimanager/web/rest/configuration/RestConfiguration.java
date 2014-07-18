package com.emmisolutions.emmimanager.web.rest.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * Spring container configuration.
 */
@Configuration
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.service.configuration",
        "com.emmisolutions.emmimanager.web.rest.spring",
        "com.emmisolutions.emmimanager.web.rest.impl"
})
@EnableSpringDataWebSupport
public class RestConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        addDefaultHttpMessageConverters(converters);

        for (HttpMessageConverter messageConverter : converters) {
            if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) messageConverter).getObjectMapper();
                // set Jackson features
                mapper.enable(SerializationFeature.INDENT_OUTPUT); // make it pretty
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // allow random properties to come in
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO8601 Formatted Dates
                mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY); // only include non-empty values

                // extend support to other types
                mapper.registerModule(new JodaModule()); // Joda Dates, only support ISO8601
                mapper.registerModule(new JaxbAnnotationModule()); // JAXB as well as Jackson Annotations
                mapper.registerModule(new Hibernate4Module()); // Hibernate (lazy loaded entities ==> null)

            }
        }
    }

}
