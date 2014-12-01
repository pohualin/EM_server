package com.emmisolutions.emmimanager.web.rest.model.user.client;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * Creates a UserResource from a User
 */
@Component
public class UserClientResourceAssembler implements ResourceAssembler<UserClient, UserClientResource> {

    @Override
    public UserClientResource toResource(UserClient entity) {
        UserClientResource ret = new UserClientResource();
        ret.setEntity(entity);
        return ret;
    }

}
