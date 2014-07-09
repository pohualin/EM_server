package com.emmisolutions.emmimanager.web.rest.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring container configuration.
 */
@Configuration
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.service.configuration",
        "com.emmisolutions.emmimanager.web.rest.impl",
        "com.emmisolutions.emmimanager.web.rest.jax_rs"
})
public class RestConfiguration {
}
