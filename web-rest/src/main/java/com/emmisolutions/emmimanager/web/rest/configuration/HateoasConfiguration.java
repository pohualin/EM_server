package com.emmisolutions.emmimanager.web.rest.configuration;

import com.emmisolutions.emmimanager.web.rest.configuration.hateoas.PagedResourcesAssemblerArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;

/**
 * Allows for specialized overriding of some Hateoas classes.
 */
@Configuration
public class HateoasConfiguration extends HateoasAwareSpringDataWebConfiguration {

    @Bean
    public PagedResourcesAssemblerArgumentResolver pagedResourcesAssemblerArgumentResolver() {
        return new PagedResourcesAssemblerArgumentResolver(pageableResolver(), null);
    }
}
