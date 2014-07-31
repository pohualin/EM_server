package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.User;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * This assembler is to be used when a User is required for a relationship with another object.
 * As such, it contains no links, and has no permissions in it.
 */
@Component("userResourceForAssociationsAssembler")
public class UserResourceForAssociationsAssembler implements ResourceAssembler<User, UserResource> {

    @Override
    public UserResource toResource(User user) {
        return new UserResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                null);
    }

}
