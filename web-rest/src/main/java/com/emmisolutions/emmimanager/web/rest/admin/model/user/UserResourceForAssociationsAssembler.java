package com.emmisolutions.emmimanager.web.rest.admin.model.user;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * This assembler is to be used when a User is required for a relationship with another object.
 * As such, it contains no links, and has no permissions in it.
 */
@Component("userResourceForAssociationsAssembler")
public class UserResourceForAssociationsAssembler implements ResourceAssembler<UserAdmin, UserResource> {

    @Override
    public UserResource toResource(UserAdmin user) {
        return new UserResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                null, 
                null);
    }

}
