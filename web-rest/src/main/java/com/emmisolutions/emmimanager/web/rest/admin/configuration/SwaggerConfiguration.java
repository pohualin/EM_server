package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.alternates.Alternates;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;

import java.util.Date;

/**
 * Configures Swagger Beans
 */
@Configuration
@EnableSwagger
public class SwaggerConfiguration {
    public static final String ADMIN_WEBAPI = "/webapi.*";

    public static final String CLIENT_WEBAPI = "/webapi-client.*";


    /**
     * Swagger Spring MVC configuration
     *
     * @param springSwaggerConfig the configuration to be tweaked
     * @return the MVC plugin
     */
    @Bean
    public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
            .apiInfo(apiInfo())
            .genericModelSubstitutes(ResponseEntity.class)
            .alternateTypeRules(Alternates.newRule(LocalDate.class, Date.class),
                Alternates.newRule(DateTime.class, Date.class))
            .ignoredParameterTypes(PagedResourcesAssembler.class, Pageable.class, Sort.class)
                .includePatterns(ADMIN_WEBAPI, CLIENT_WEBAPI);
    }

    /**
     * API Info as it appears on the swagger-ui page
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
            "Web REST API",
            "This is the server side REST API used by the Emmi Manager application.",
            null,
            null,
            null,
            null);
    }
}
