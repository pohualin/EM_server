package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.service.UserClientService;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class ClientUserClientValidationErrorResourceAssembler
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