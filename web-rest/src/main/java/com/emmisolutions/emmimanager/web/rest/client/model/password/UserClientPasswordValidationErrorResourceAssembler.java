package com.emmisolutions.emmimanager.web.rest.client.model.password;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;

/**
 * Assembler to convert an UserClientPasswordValidationError to
 * UserClientPasswordValidationErrorResource
 *
 */
@Component
public class UserClientPasswordValidationErrorResourceAssembler
        implements
        ResourceAssembler<UserClientPasswordValidationError, UserClientPasswordValidationErrorResource> {

    @Override
    public UserClientPasswordValidationErrorResource toResource(
            UserClientPasswordValidationError userClientPasswordValidationError) {
        if (userClientPasswordValidationError == null) {
            return null;
        }

        UserClientPasswordValidationErrorResource ret = new UserClientPasswordValidationErrorResource();
        ret.setEntity(userClientPasswordValidationError);

        return ret;
    }

}
