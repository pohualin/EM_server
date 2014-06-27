package com.emmisolutions.emmimanager.web.rest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * This class is necessary to write Joda dates and times properly to JSON. It
 * uses Spring's Jackson2ObjectMapperFactoryBean as the creator of the ObjectMapper
 *
 * @see org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean
 */
@Provider
public class JacksonObjectMapper implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        // use Spring's Wrapper class to create the mapper factory
        Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
        bean.setIndentOutput(true);
        bean.setSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        bean.afterPropertiesSet();
        return bean.getObject();
    }
}
