package com.emmisolutions.emmimanager.web.rest.endpoint;

import com.emmisolutions.emmimanager.api.UserCrud;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Sample JAX-RS endpoint. @Scope should work as expected across all scopes.. make sure to use @Component to
 * hook up spring resources as well as @Path to expose the REST endpoints
 */
@Path("/user")
@Component
@Scope("prototype")
public class UserEndpoint implements UserCrud {

    @Resource
    UserService userService;

    @GET
    @Produces(APPLICATION_JSON)
    public User getUser() {
        return userService.save(new User());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public void updateUser(User user) {
        // no-op
    }

    @PUT
    @Produces(APPLICATION_JSON)
    public User save(User user) {
        return user;
    }

    @DELETE
    public void delete(User user) {
        // no-op
    }
}
