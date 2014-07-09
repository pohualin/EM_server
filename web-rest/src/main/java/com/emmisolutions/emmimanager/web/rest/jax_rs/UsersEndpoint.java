package com.emmisolutions.emmimanager.web.rest.jax_rs;

import com.emmisolutions.emmimanager.api.UserApi;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.model.UserResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * Sample JAX-RS endpoint. @Scope should work as expected across all scopes.. make sure to use @Component to
 * hook up spring resources as well as @Path to expose the REST endpoints
 */
@Path("")
@Component
@Produces({
        APPLICATION_JSON,
        APPLICATION_XML})
@Consumes({
        APPLICATION_JSON,
        APPLICATION_XML})
@Scope("prototype")
public class UsersEndpoint {

    @Resource
    UserApi userApi;
    String basePath;

    @GET
    @Path("authenticated")
    public Response authenticated() {
        return generateResponse(userApi.loggedIn());
    }

    @GET
    @Path("users/{id}")
    public Response get(@PathParam("id") User user) {
        return generateResponse(userApi.getUser(user));
    }

    @POST
    @Path("users")
    public Response create(User user) {
        return generateResponse(userApi.create(user));
    }

    @PUT
    @Path("users")
    public Response update(User user) {
        return generateResponse(userApi.save(user));
    }

    @DELETE
    @Path("users/{id}")
    public void delete(@PathParam("id") User user) {
        userApi.delete(user);
    }


    private Response generateResponse(User user) {
        if (user == null) {
            return Response.noContent().build();
        } else {
            return Response
                    .ok(new UserResource(user, basePath))
                    .build();
        }
    }

    @Context
    public void setUriInfo(UriInfo uriInfo) {
        basePath = uriInfo.getBaseUri().getPath();
    }
}
