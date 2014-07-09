package com.emmisolutions.emmimanager.web.rest.endpoint;

import com.emmisolutions.emmimanager.api.UserCrud;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * Sample JAX-RS endpoint. @Scope should work as expected across all scopes.. make sure to use @Component to
 * hook up spring resources as well as @Path to expose the REST endpoints
 */
@Path("/users")
@Component
@Produces({
        APPLICATION_JSON,
        APPLICATION_XML})
@Consumes({
        APPLICATION_JSON,
        APPLICATION_XML})
@Scope("prototype")
public class UsersEndpoint implements UserCrud {

    @Resource
    UserService userService;

    @GET
    public User getUser() {
        return userService.save(new User());
    }

    @POST
    public void updateUser(User user) {
        // no-op
    }

    @PUT
    public User save(User user) {
        return user;
    }

    @DELETE
    public void delete(User user) {
        // no-op
    }
}
