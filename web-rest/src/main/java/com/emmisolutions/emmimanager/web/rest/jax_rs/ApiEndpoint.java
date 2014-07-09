package com.emmisolutions.emmimanager.web.rest.jax_rs;

import com.emmisolutions.emmimanager.web.rest.model.PublicApi;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * The outward facing API for the application.
 */
@Path("")
@Produces({
        APPLICATION_JSON,
        APPLICATION_XML})
@Consumes({
        APPLICATION_JSON,
        APPLICATION_XML})
@Component
@Scope("prototype")
public class ApiEndpoint {

    String basePath;

    @GET
    @Path("/")
    public PublicApi get() {
        return new PublicApi(basePath);
    }

    @Context
    public void setUriInfo(UriInfo uriInfo) {
        basePath = uriInfo.getBaseUri().getPath();
    }
}
