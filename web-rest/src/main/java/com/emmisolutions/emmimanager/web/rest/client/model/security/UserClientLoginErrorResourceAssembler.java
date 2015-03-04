package com.emmisolutions.emmimanager.web.rest.client.model.security;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;

/**
 * 
 * Creates a UserClientLoginErrorResource from a UserClientLoginError
 *
 */
@Component
public class UserClientLoginErrorResourceAssembler implements
        ResourceAssembler<UserClientLoginError, UserClientLoginErrorResource> {

    @Resource(name = "userClientClientResourceAssembler")
    ResourceAssembler<Client, ClientResource> clientResourceAssembler;

    @Override
    public UserClientLoginErrorResource toResource(
            UserClientLoginError userClientLoginFailure) {
        if (userClientLoginFailure == null) {
            return null;
        }

        UserClientLoginErrorResource ret = new UserClientLoginErrorResource();
        ret.setEntity(userClientLoginFailure);

        if (userClientLoginFailure.getClient() != null) {
            ret.setClientResource(clientResourceAssembler
                    .toResource(userClientLoginFailure.getClient()));
        }

        return ret;
    }

}
