package com.emmisolutions.emmimanager.web.rest.endpoint;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * The outward facing API for the application.
 */
@Path("public")
@Produces({
        APPLICATION_JSON,
        APPLICATION_XML})
@Consumes({
        APPLICATION_JSON,
        APPLICATION_XML})
@Component
@Scope("prototype")
public class ApiEndpoint {


}
