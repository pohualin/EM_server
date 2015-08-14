package com.emmisolutions.emmimanager.persistence.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 'Extra' beans that are only needed for testing purposes but will probably be required for runtime.
 */
@Configuration
public class PersistenceTestConfiguration {

    /**
     * Need this bean for any classes that use the RestTemplate class to read
     * any JSON coming back
     *
     * @return the converter
     */
    @Bean
    public MappingJackson2HttpMessageConverter jsonJacksonConverter() {
        return new MappingJackson2HttpMessageConverter();
    }
}
