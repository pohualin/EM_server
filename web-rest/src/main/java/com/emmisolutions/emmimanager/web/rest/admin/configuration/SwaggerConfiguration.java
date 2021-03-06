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

import javax.annotation.Resource;
import java.util.Date;

/**
 * Configures Swagger Beans
 */
@Configuration
@EnableSwagger
public class SwaggerConfiguration {
    public static final String ADMIN_WEBAPI = "/webapi.*";

    public static final String CLIENT_WEBAPI = "/webapi-client.*";

    @Resource
    SpringSwaggerConfig springSwaggerConfig;

    /**
     * Swagger Spring MVC configuration
     *
     * @return the MVC plugin
     */
    @Bean(name = "adminSwaggerSpringMvcPlugin")
    public SwaggerSpringMvcPlugin adminSwaggerSpringMvcPlugin() {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(new ApiInfo(
                        "Web REST API",
                        "This is the server side REST API used by the Emmi Manager application.",
                        null,
                        null,
                        null,
                        null))
                .swaggerGroup("admin")
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(Alternates.newRule(LocalDate.class, Date.class),
                        Alternates.newRule(DateTime.class, Date.class))
                .ignoredParameterTypes(PagedResourcesAssembler.class, Pageable.class, Sort.class)

                .includePatterns(ADMIN_WEBAPI);
    }

    /**
     * Swagger Spring MVC configuration
     *
     * @return the MVC plugin
     */
    @Bean(name = "clientSwaggerSpringMvcPlugin")
    public SwaggerSpringMvcPlugin clientSwaggerSpringMvcPlugin() {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(new ApiInfo(
                        "Client Web REST API",
                        "This is the server side REST API used by the client facing Emmi Manager application.",
                        null,
                        null,
                        null,
                        null))
                .swaggerGroup("client")
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(Alternates.newRule(LocalDate.class, Date.class),
                        Alternates.newRule(DateTime.class, Date.class))
                .ignoredParameterTypes(PagedResourcesAssembler.class, Pageable.class, Sort.class)

                .includePatterns(CLIENT_WEBAPI);
    }

}
