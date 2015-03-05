package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.service.UserClientService;

/**
 */
@Component
public class UserClientValidationErrorResourceAssembler
        implements
        ResourceAssembler<UserClientService.UserClientValidationError, UserClientResource> {

    @Override
    public UserClientResource toResource(
            UserClientService.UserClientValidationError validationError) {
        UserClientResource ret = new UserClientResource();
        ret.setValidationError(validationError);
        return ret;
    }
}
