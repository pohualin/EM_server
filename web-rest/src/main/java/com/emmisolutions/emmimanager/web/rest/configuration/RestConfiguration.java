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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.GeoModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

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

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        addDefaultHttpMessageConverters(converters);
        converters.add(new JsonJacksonConverter());
        converters.add(new XmlJacksonConverter());
    }

    private void enableJacksonFeatures(ObjectMapper mapper){
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // make it pretty
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // allow random properties to come in
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO8601 Formatted Dates
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY); // don't write null values in JSON
        mapper.registerModule(new JodaModule()); // Joda Dates, only support ISO8601
        mapper.registerModule(new JaxbAnnotationModule()); // JAXB as well as Jackson Annotations
        mapper.registerModule(new Hibernate4Module()); // Hibernate (lazy loaded entities ==> null)
        mapper.registerModule(new JacksonXmlModule());
        mapper.registerModule(new GeoModule()); // spatial mappings via spring-data
    }

    private class XmlJacksonConverter extends MappingJackson2HttpMessageConverter{
        public XmlJacksonConverter(){
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

    private class JsonJacksonConverter extends MappingJackson2HttpMessageConverter{
        public JsonJacksonConverter(){
            enableJacksonFeatures(getObjectMapper());
        }
    }
}
