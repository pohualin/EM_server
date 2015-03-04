package com.emmisolutions.emmimanager.web.rest.client.model.security;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * 
 * Creates a UserClientLoginErrorResource from a UserClientLoginError
 *
 */
@Component
public class UserClientLoginErrorResourceAssembler implements
        ResourceAssembler<UserClientLoginError, UserClientLoginErrorResource> {

    @Override
    public UserClientLoginErrorResource toResource(
            UserClientLoginError userClientLoginFailure) {
        if (userClientLoginFailure == null) {
            return null;
        }
        UserClientLoginErrorResource ret = new UserClientLoginErrorResource();
        ret.setEntity(userClientLoginFailure);
        return ret;
    }

}
