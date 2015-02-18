package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.service.UserClientService;

/**
 * Creates a UserClientResource from a UserClient
 */
@Component
public class UserClientRestrictedEmailResourceAssembler
        implements
        ResourceAssembler<UserClientService.UserClientRestrictedEmail, UserClientResource> {

    @Override
    public UserClientResource toResource(
            UserClientService.UserClientRestrictedEmail restrictedEmail) {
        UserClientResource ret = null;
        if (restrictedEmail != null) {
            ret = new UserClientResource();
            ret.setRestrictedEmail(restrictedEmail);
        }
        return ret;
    }
}
