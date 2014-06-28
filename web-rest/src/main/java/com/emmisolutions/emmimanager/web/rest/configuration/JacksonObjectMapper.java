package com.emmisolutions.emmimanager.web.rest.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * This class is where we configure all of the Jackson JSON features.
 */
@Provider
public class JacksonObjectMapper implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public ObjectMapper getContext(Class<?> type) {

        // set Jackson features
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // make it pretty
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // allow random properties to come in
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO8601 Formatted Dates

        // extend support to other types
        mapper.registerModule(new JodaModule()); // Joda Dates, only support ISO8601
        mapper.registerModule(new JaxbAnnotationModule()); // JAXB as well as Jackson Annotations
        mapper.registerModule(new Hibernate4Module()); // Hibernate (lazy loaded entities ==> null)

        return mapper;
    }
}
